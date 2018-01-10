cmake_minimum_required(VERSION 2.8)
project(slam_SDVL)
option(USE_ANDROID "Android Cross Compilation" OFF)
set(USE_PANGOLIN ON)
set(DEBUG OFF)

include (FindPkgConfig)
if (PKG_CONFIG_FOUND)
  FIND_PACKAGE(Threads REQUIRED)
  FIND_PACKAGE(OpenCV REQUIRED)
  pkg_check_modules(Eigen REQUIRED eigen3)
  if(USE_GUI)
    FIND_PACKAGE(Pangolin REQUIRED)
  endif()
else()
 set(PKG_CONFIG_EXECUTABLE "/usr/bin/pkg-config")
endif()

IF(NOT CMAKE_BUILD_TYPE)
  SET(CMAKE_BUILD_TYPE Release)
ENDIF()

MESSAGE("Build type: " ${CMAKE_BUILD_TYPE})

if(USE_ANDROID)
  MESSAGE(STATUS "Compiling for Android")

  ADD_DEFINITIONS(-DANDROID)

  set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS}  -Wall -O3")
  set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -O3 -std=c++11")

  set(EIGEN3_INCLUDE_DIR /usr/include/eigen3)
  set(OPENCV_SDK /home/seriznue/OpenCV-android-sdk)

  # Configure OpenCV
  include_directories(${OPENCV_SDK}/sdk/native/jni/include)
  add_library(lib_opencv SHARED IMPORTED)
  set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${OPENCV_SDK}/sdk/native/libs/${ANDROID_ABI}/libopencv_java3.so)
else()
  MESSAGE(STATUS "Compiling on Unix")

  if(USE_PANGOLIN)
    ADD_DEFINITIONS(-DPANGOLIN)
  endif()

  if(DEBUG)
    ADD_DEFINITIONS(-ggdb)
  endif()

  set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS}  -Wall -O3 -march=native ")
  set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -O3 -march=native -std=c++11")

  LIST(APPEND CMAKE_MODULE_PATH ${PROJECT_SOURCE_DIR}/cmake_modules)

  find_package(OpenCV 3.0 QUIET)
  if(NOT OpenCV_FOUND)
     find_package(OpenCV 2.4.9.1 REQUIRED)
  endif()
  find_package(Eigen3 3.1.0 REQUIRED)
  if(USE_PANGOLIN)
    find_package(Pangolin REQUIRED)
  endif()
endif()

include_directories(
  ${PROJECT_SOURCE_DIR}
  ${PROJECT_SOURCE_DIR}/src
  ${EIGEN3_INCLUDE_DIR}
)

if(USE_PANGOLIN)
  include_directories(${Pangolin_INCLUDE_DIRS})
endif()

if(USE_ANDROID)
  set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib/${ANDROID_ABI})
else()
  set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib)
endif()

# Set SD-SLAM sourcefiles
LIST(APPEND SOURCEFILES
  #Main
  sdvl.cc video_source.cc camera.cc point.cc frame.cc feature.cc config.cc
  #Mapping
  map.cc homography_init.cc
  #Tracking
  image_align.cc feature_align.cc matcher.cc
  #Extra classes
  extra/se3.cc extra/fast_detector.cc extra/orb_detector.cc extra/utils.cc extra/bundle.cc
  #g2o
  extra/g2o/g2o/core/base_binary_edge.h
  extra/g2o/g2o/core/block_solver.hpp
  extra/g2o/g2o/core/hyper_graph_action.h
  extra/g2o/g2o/core/optimizable_graph.cpp
  extra/g2o/g2o/core/optimization_algorithm_property.h
  extra/g2o/g2o/core/robust_kernel_impl.h
  extra/g2o/g2o/core/base_binary_edge.hpp
  extra/g2o/g2o/core/cache.cpp
  extra/g2o/g2o/core/hyper_graph.cpp
  extra/g2o/g2o/core/optimizable_graph.h
  extra/g2o/g2o/core/optimization_algorithm_with_hessian.cpp
  extra/g2o/g2o/core/solver.cpp
  extra/g2o/g2o/core/base_edge.h
  extra/g2o/g2o/core/cache.h
  extra/g2o/g2o/core/hyper_graph.h
  extra/g2o/g2o/core/optimization_algorithm.cpp
  extra/g2o/g2o/core/optimization_algorithm_with_hessian.h
  extra/g2o/g2o/core/solver.h
  extra/g2o/g2o/core/base_multi_edge.h
  extra/g2o/g2o/core/creators.h
  extra/g2o/g2o/core/jacobian_workspace.cpp
  extra/g2o/g2o/core/optimization_algorithm_dogleg.cpp
  extra/g2o/g2o/core/parameter_container.cpp
  extra/g2o/g2o/core/sparse_block_matrix_ccs.h
    extra/g2o/g2o/core/base_multi_edge.hpp
    extra/g2o/g2o/core/eigen_types.h
    extra/g2o/g2o/core/jacobian_workspace.h
    extra/g2o/g2o/core/optimization_algorithm_dogleg.h
    extra/g2o/g2o/core/parameter_container.h
    extra/g2o/g2o/core/sparse_block_matrix_diagonal.h
  extra/g2o/g2o/core/base_unary_edge.h
  extra/g2o/g2o/core/estimate_propagator.cpp
  extra/g2o/g2o/core/linear_solver.h
  extra/g2o/g2o/core/optimization_algorithm_factory.cpp
  extra/g2o/g2o/core/parameter.cpp
  extra/g2o/g2o/core/sparse_block_matrix.h
  extra/g2o/g2o/core/base_unary_edge.hpp
  extra/g2o/g2o/core/estimate_propagator.h
  extra/g2o/g2o/core/marginal_covariance_cholesky.cpp
  extra/g2o/g2o/core/optimization_algorithm_factory.h
  extra/g2o/g2o/core/parameter.h
  extra/g2o/g2o/core/sparse_block_matrix.hpp
  extra/g2o/g2o/core/base_vertex.h
  extra/g2o/g2o/core/factory.cpp
  extra/g2o/g2o/core/marginal_covariance_cholesky.h
  extra/g2o/g2o/core/optimization_algorithm_gauss_newton.cpp
  extra/g2o/g2o/core/robust_kernel.cpp
  extra/g2o/g2o/core/sparse_block_matrix_test.cpp
  extra/g2o/g2o/core/base_vertex.hpp
  extra/g2o/g2o/core/factory.h
  extra/g2o/g2o/core/matrix_operations.h
  extra/g2o/g2o/core/optimization_algorithm_gauss_newton.h
  extra/g2o/g2o/core/robust_kernel_factory.cpp
  extra/g2o/g2o/core/sparse_optimizer.cpp
  extra/g2o/g2o/core/batch_stats.cpp
  extra/g2o/g2o/core/hyper_dijkstra.cpp
  extra/g2o/g2o/core/matrix_structure.cpp
  extra/g2o/g2o/core/optimization_algorithm.h
  extra/g2o/g2o/core/robust_kernel_factory.h
  extra/g2o/g2o/core/sparse_optimizer.h
  extra/g2o/g2o/core/batch_stats.h
  extra/g2o/g2o/core/hyper_dijkstra.h
  extra/g2o/g2o/core/matrix_structure.h
  extra/g2o/g2o/core/optimization_algorithm_levenberg.cpp
  extra/g2o/g2o/core/robust_kernel.h
  extra/g2o/g2o/core/block_solver.h
  extra/g2o/g2o/core/hyper_graph_action.cpp
  extra/g2o/g2o/core/openmp_mutex.h
  extra/g2o/g2o/core/optimization_algorithm_levenberg.h
  extra/g2o/g2o/core/robust_kernel_impl.cpp
  extra/g2o/g2o/solvers/linear_solver_dense.h
  extra/g2o/g2o/solvers/linear_solver_eigen.h
  extra/g2o/g2o/stuff/color_macros.h
  extra/g2o/g2o/stuff/macros.h
  extra/g2o/g2o/stuff/misc.h
  extra/g2o/g2o/stuff/os_specific.c
  extra/g2o/g2o/stuff/os_specific.h
  extra/g2o/g2o/stuff/property.cpp
  extra/g2o/g2o/stuff/property.h
  extra/g2o/g2o/stuff/string_tools.cpp
  extra/g2o/g2o/stuff/string_tools.h
  extra/g2o/g2o/stuff/timeutil.cpp
  extra/g2o/g2o/stuff/timeutil.h
  extra/g2o/g2o/types/se3_ops.h
  extra/g2o/g2o/types/se3_ops.hpp
  extra/g2o/g2o/types/se3quat.h
  extra/g2o/g2o/types/sim3.h
  extra/g2o/g2o/types/types_sba.cpp
  extra/g2o/g2o/types/types_sba.h
  extra/g2o/g2o/types/types_seven_dof_expmap.cpp
  extra/g2o/g2o/types/types_seven_dof_expmap.h
  extra/g2o/g2o/types/types_six_dof_expmap.cpp
  extra/g2o/g2o/types/types_six_dof_expmap.h
)

if(NOT USE_ANDROID AND USE_PANGOLIN)
  LIST(APPEND SOURCEFILES
    # UI
    ui/drawimage.cc ui/drawscene.cc ui/ui.cc
  )
endif()


add_library(${PROJECT_NAME} SHARED ${SOURCEFILES})

if(USE_ANDROID)
  target_link_libraries(${PROJECT_NAME} lib_opencv log)
else()
  if(USE_PANGOLIN)
    target_link_libraries(${PROJECT_NAME} ${OpenCV_LIBS} ${Pangolin_LIBRARIES})
  else()
    target_link_libraries(${PROJECT_NAME} ${OpenCV_LIBS})
  endif()
endif()

#if(NOT USE_ANDROID)
#  # Build examples
#  set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR})

#  add_executable(Main
#  main.cc)
#  target_link_libraries(Main ${PROJECT_NAME})
#endif()

