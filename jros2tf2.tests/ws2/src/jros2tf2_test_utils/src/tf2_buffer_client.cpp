#include <functional>
#include <future>
#include <memory>
#include <string>
#include <sstream>

#include "rclcpp/rclcpp.hpp"
#include "rclcpp_action/rclcpp_action.hpp"
#include "rclcpp_components/register_node_macro.hpp"


#include "tf2_ros/buffer_client.h"
#include "geometry_msgs/msg/transform_stamped.hpp"
#include "tf2_msgs/action/lookup_transform.hpp"

namespace jros2tf2_test_utils
{
class Tf2BufferClient : public rclcpp::Node
{
public:
  using LookupTransform = tf2_msgs::action::LookupTransform;
  using GoalHandle = rclcpp_action::ClientGoalHandle<LookupTransform>;

  explicit Tf2BufferClient(const rclcpp::NodeOptions & options)
  : Node("tf2_buffer_client", options)
  {
    this->client_ptr_ = rclcpp_action::create_client<LookupTransform>(
      this,
      "tf2_buffer_server");

    this->timer_ = this->create_wall_timer(
      std::chrono::milliseconds(500),
      std::bind(&Tf2BufferClient::send_goal, this));
  }

  void send_goal()
  {
    using namespace std::placeholders;

    this->timer_->cancel();
    RCLCPP_INFO(this->get_logger(), "Sending goal");
    if (!this->client_ptr_->wait_for_action_server()) {
      RCLCPP_ERROR(this->get_logger(), "Action server not available after waiting");
      rclcpp::shutdown();
    }

    auto goal_msg = LookupTransform::Goal();
    goal_msg.target_frame = "world";
    goal_msg.source_frame = "panda_hand";
    goal_msg.source_time = tf2_ros::toMsg(tf2::timeFromSec(0)/*tf2::get_now()*/);
    goal_msg.timeout = tf2_ros::toMsg(tf2::durationFromSec(3));
    goal_msg.advanced = false;

    RCLCPP_INFO(this->get_logger(), "Sending goal");

    auto send_goal_options = rclcpp_action::Client<LookupTransform>::SendGoalOptions();
    send_goal_options.goal_response_callback =
      std::bind(&Tf2BufferClient::goal_response_callback, this, _1);
    send_goal_options.feedback_callback =
      std::bind(&Tf2BufferClient::feedback_callback, this, _1, _2);
    send_goal_options.result_callback =
      std::bind(&Tf2BufferClient::result_callback, this, _1);
    this->client_ptr_->async_send_goal(goal_msg, send_goal_options);
  }

private:
  rclcpp_action::Client<LookupTransform>::SharedPtr client_ptr_;
  rclcpp::TimerBase::SharedPtr timer_;

  void goal_response_callback(const GoalHandle::SharedPtr & goal_handle)
  {
    if (!goal_handle) {
      RCLCPP_ERROR(this->get_logger(), "Goal was rejected by server");
    } else {
      RCLCPP_INFO(this->get_logger(), "Goal accepted by server, waiting for result");
    }
  }

  void feedback_callback(
    GoalHandle::SharedPtr,
    const std::shared_ptr<const LookupTransform::Feedback> feedback)
  {
    std::stringstream ss;
    ss << "Next number in sequence received: ";
    RCLCPP_INFO(this->get_logger(), ss.str().c_str());
  }

  void result_callback(const GoalHandle::WrappedResult & result)
  {
    switch (result.code) {
      case rclcpp_action::ResultCode::SUCCEEDED:
        break;
      case rclcpp_action::ResultCode::ABORTED:
        RCLCPP_ERROR(this->get_logger(), "Goal was aborted");
        return;
      case rclcpp_action::ResultCode::CANCELED:
        RCLCPP_ERROR(this->get_logger(), "Goal was canceled");
        return;
      default:
        RCLCPP_ERROR(this->get_logger(), "Unknown result code");
        return;
    }
    std::stringstream ss;
    geometry_msgs::msg::TransformStamped transformStamped = result.result->transform;
    ss << "Result received: " << transformStamped.transform.translation.x << " " << transformStamped.transform.translation.y << " " << transformStamped.transform.translation.z;
    RCLCPP_INFO(this->get_logger(), ss.str().c_str());
    rclcpp::shutdown();
  }
};  // class Tf2BufferClient

}  // namespace jros2tf2_test_utils

int main(int argc, char **argv)
{
    rclcpp::init(argc, argv);
    auto node = std::make_shared<jros2tf2_test_utils::Tf2BufferClient>(rclcpp::NodeOptions());
    rclcpp::spin(node);
    rclcpp::shutdown();
    return 0;
}
