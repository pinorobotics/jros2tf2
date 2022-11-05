cmake_minimum_required(VERSION 3.5)
project(jros2tf2_test_utils)

# Default to C++14
if(NOT CMAKE_CXX_STANDARD)
  set(CMAKE_CXX_STANDARD 14)
endif()

if(CMAKE_COMPILER_IS_GNUCXX OR CMAKE_CXX_COMPILER_ID MATCHES "Clang")
  add_compile_options(-Wall -Wextra -Wpedantic -Wunused-parameter)
endif()

find_package(ament_cmake REQUIRED)
find_package(tf2 REQUIRED)
find_package(tf2_ros REQUIRED)
find_package(rclcpp REQUIRED)
find_package(rclcpp_action REQUIRED)
find_package(rclcpp_components REQUIRED)

add_executable(tf2_buffer_client
  src/tf2_buffer_client.cpp)
ament_target_dependencies(tf2_buffer_client
  "tf2_ros"
  "rclcpp"
  "rclcpp_action"
  "rclcpp_components")

# install executables
# using bin folder does not work for some reason
install(TARGETS tf2_buffer_client
  DESTINATION lib/${PROJECT_NAME})

if(BUILD_TESTING)
  find_package(ament_lint_auto REQUIRED)
  ament_lint_auto_find_test_dependencies()
endif()

ament_package()


