#include <com_appdimension_pruebaopencv_NativeClass.h>
#include <android/log.h>

#define LOG_TAG "FaceDetection"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))

JNIEXPORT jstring JNICALL Java_com_appdimension_pruebaopencv_NativeClass_getMessageFromJNI
  (JNIEnv *env, jclass object)
  {
    return env->NewStringUTF("this is a message from JNI");
  }

JNIEXPORT void JNICALL Java_com_appdimension_pruebaopencv_NativeClass_faceDetection
  (JNIEnv *, jclass, jlong addrRgba)
  {
    Mat& frame = *(Mat*)addrRgba;

    detect(frame);
  }

  void detect(Mat& frame)
  {
     /** Global variables */

     //String face_cascade_name = "/storage/emulated/0/data/haarcascade_frontalface_alt.xml";
     //String eyes_cascade_name = "/storage/emulated/0/data/haarcascade_eye_tree_eyeglasses.xml";


     String face_cascade_name = "/home/studio/AndroidStudioProjects/OpenCV-android-sdk/haarcascade_frontalface_alt.xml";
     String eyes_cascade_name = "/home/studio/AndroidStudioProjects/OpenCV-android-sdk/haarcascade_eye_tree_eyeglasses.xml";

     CascadeClassifier face_cascade;
     CascadeClassifier eyes_cascade;

     //-- 1. Load the cascades
        if( !face_cascade.load( face_cascade_name ) )
        {
            LOGD("Xml not loaded");
            printf("--(!)Error loading\n"); return ;
        };

        if( !eyes_cascade.load( eyes_cascade_name ) )
        {
            LOGD("Xml not loaded");
            printf("--(!)Error loading\n"); return ;
        };


        std::vector<Rect> faces;
          Mat frame_gray;

          cvtColor( frame, frame_gray, CV_BGR2GRAY );
          equalizeHist( frame_gray, frame_gray );

          //-- Detect faces
          face_cascade.detectMultiScale( frame_gray, faces, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30) );

          for( size_t i = 0; i < faces.size(); i++ )
          {
            Point center( faces[i].x + faces[i].width*0.5, faces[i].y + faces[i].height*0.5 );
            ellipse( frame, center, Size( faces[i].width*0.5, faces[i].height*0.5), 0, 0, 360, Scalar( 255, 0, 255 ), 4, 8, 0 );

            Mat faceROI = frame_gray( faces[i] );
            std::vector<Rect> eyes;

            //-- In each face, detect eyes
            eyes_cascade.detectMultiScale( faceROI, eyes, 1.1, 2, 0 |CV_HAAR_SCALE_IMAGE, Size(30, 30) );

            for( size_t j = 0; j < eyes.size(); j++ )
             {
               Point center( faces[i].x + eyes[j].x + eyes[j].width*0.5, faces[i].y + eyes[j].y + eyes[j].height*0.5 );
               int radius = cvRound( (eyes[j].width + eyes[j].height)*0.25 );
               circle( frame, center, radius, Scalar( 255, 0, 0 ), 4, 8, 0 );
             }
          }
  }

  JNIEXPORT void JNICALL Java_com_appdimension_pruebaopencv_NativeClass_faceDetectionXML
    (JNIEnv *env, jclass, jlong addrRgba, jstring pathFrontalFace, jstring pathEyes)
    {
        if(pathFrontalFace == NULL)
        {
            LOGD("pathfrontalface esta null");
        }
        else
        {
            LOGD("pathfrontalface esta bien");
        }


      Mat& frame = *(Mat*)addrRgba;

        std::string str;
        GetJStringContent(env,pathFrontalFace,str);
        const char *fullPathFrontalFace = str.append("").c_str();

        std::string str2;
        GetJStringContent(env,pathEyes,str2);
        const char *fullPathEyes = str2.append("").c_str();

            //LOGD("lo que llego");
            //LOGD(fullPathFrontalFace);
      detectXML(frame, fullPathFrontalFace, fullPathEyes);

    }

    void detectXML(Mat& frame, String pathFrontalFace, String pathEyes)
    {
       /** Global variables */

       //String face_cascade_name = "/storage/emulated/0/data/haarcascade_frontalface_alt.xml";
       //String eyes_cascade_name = "/storage/emulated/0/data/haarcascade_eye_tree_eyeglasses.xml";


       String face_cascade_name = pathFrontalFace;
       String eyes_cascade_name = pathEyes;

       CascadeClassifier face_cascade;
       CascadeClassifier eyes_cascade;

       //-- 1. Load the cascades
          if( !face_cascade.load( face_cascade_name ) )
          {
              LOGD("Xml not loaded");
              printf("--(!)Error loading\n"); return ;
          };

          if( !eyes_cascade.load( eyes_cascade_name ) )
          {
              LOGD("Xml not loaded");
              printf("--(!)Error loading\n"); return ;
          };


          std::vector<Rect> faces;
            Mat frame_gray;

            cvtColor( frame, frame_gray, CV_BGR2GRAY );
            equalizeHist( frame_gray, frame_gray );

            //-- Detect faces
            face_cascade.detectMultiScale( frame_gray, faces, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30) );

            for( size_t i = 0; i < faces.size(); i++ )
            {
              Point center( faces[i].x + faces[i].width*0.5, faces[i].y + faces[i].height*0.5 );
              ellipse( frame, center, Size( faces[i].width*0.5, faces[i].height*0.5), 0, 0, 360, Scalar( 255, 0, 255 ), 4, 8, 0 );

              Mat faceROI = frame_gray( faces[i] );
              std::vector<Rect> eyes;

              //-- In each face, detect eyes
              eyes_cascade.detectMultiScale( faceROI, eyes, 1.1, 2, 0 |CV_HAAR_SCALE_IMAGE, Size(30, 30) );

              for( size_t j = 0; j < eyes.size(); j++ )
               {
                 Point center( faces[i].x + eyes[j].x + eyes[j].width*0.5, faces[i].y + eyes[j].y + eyes[j].height*0.5 );
                 int radius = cvRound( (eyes[j].width + eyes[j].height)*0.25 );
                 circle( frame, center, radius, Scalar( 255, 0, 0 ), 4, 8, 0 );
               }
            }
    }

    void GetJStringContent(JNIEnv *AEnv, jstring AStr, std::string &ARes) {
      if (!AStr) {
        ARes.clear();
        return;
      }

      const char *s = AEnv->GetStringUTFChars(AStr,NULL);
      ARes=s;
      AEnv->ReleaseStringUTFChars(AStr,s);
    }

    JNIEXPORT void JNICALL Java_com_appdimension_pruebaopencv_NativeClass_humanDetectionXML
        (JNIEnv *env, jclass, jlong addrRgba, jstring pathHuman)
        {
            if(pathHuman == NULL)
            {
                LOGD("pathhuman esta null");
            }
            else
            {
                LOGD("pathhuman esta bien");
            }


          Mat& frame = *(Mat*)addrRgba;

            std::string str;
            GetJStringContent(env,pathHuman,str);
            const char *fullPathHuman = str.append("").c_str();
                //LOGD("lo que llego");
                //LOGD(fullPathFrontalFace);
          detectHumanXML(frame, fullPathHuman);

        }

        void detectHumanXML(Mat& frame, String pathHuman)
            {
               /** Global variables */
               //String face_cascade_name = "/storage/emulated/0/data/haarcascade_frontalface_alt.xml";

               String human_cascade_name = pathHuman;


               CascadeClassifier human_cascade;


               //-- 1. Load the cascades
                  if( !human_cascade.load( human_cascade_name ) )
                  {
                      LOGD("Xml not loaded");
                      printf("--(!)Error loading\n"); return ;
                  };


                  std::vector<Rect> humans;
                    Mat frame_gray;

                    cvtColor( frame, frame_gray, CV_BGR2GRAY );
                    equalizeHist( frame_gray, frame_gray );

                    //-- Detect faces
                    human_cascade.detectMultiScale( frame_gray, humans, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30) );

                    for( int i = 0; i < humans.size(); i++ )
                    {
                        rectangle(frame, Point(humans[i].x, humans[i].y), Point(humans[i].x + humans[i].width, humans[i].y + humans[i].height), Scalar(0, 255, 0));
                    }
                    /*
                    for( size_t i = 0; i < faces.size(); i++ )
                    {
                      Point center( faces[i].x + faces[i].width*0.5, faces[i].y + faces[i].height*0.5 );
                      ellipse( frame, center, Size( faces[i].width*0.5, faces[i].height*0.5), 0, 0, 360, Scalar( 255, 0, 255 ), 4, 8, 0 );

                      Mat faceROI = frame_gray( faces[i] );
                      std::vector<Rect> eyes;

                      //-- In each face, detect eyes
                      eyes_cascade.detectMultiScale( faceROI, eyes, 1.1, 2, 0 |CV_HAAR_SCALE_IMAGE, Size(30, 30) );

                      for( size_t j = 0; j < eyes.size(); j++ )
                       {
                         Point center( faces[i].x + eyes[j].x + eyes[j].width*0.5, faces[i].y + eyes[j].y + eyes[j].height*0.5 );
                         int radius = cvRound( (eyes[j].width + eyes[j].height)*0.25 );
                         circle( frame, center, radius, Scalar( 255, 0, 0 ), 4, 8, 0 );
                       }
                    }
                    */
            }

