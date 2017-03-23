#include <com_appdimension_pruebaopencv_NativeClass.h>

JNIEXPORT jstring JNICALL Java_com_appdimension_pruebaopencv_NativeClass_getMessageFromJNI
  (JNIEnv *env, jclass object)
  {
    return env->NewStringUTF("this is a message from JNI");
  }

