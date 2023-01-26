# K0mraid3s System Shell
As seen on XDA https://forum.xda-developers.com/t/system-shell-exploit-all-samsung-mobile-devices-no-bl-unlock-required.4543071/

# Usage

### Prerequisites
- Samsung text-to-speech engine battery optimization set to unrestricted

### Steps
1. Install the `app-release.apk` in the latest [release](https://github.com/zt64/K0mraid3s_System_Shell-Source/releases/latest)
2. Reboot the device
3. Run the script in `assets`
    - If you're on Linux this is `exploit.sh`
    - If you're on Windows this is `exploit.bat`
4. If all goes well, the script should say 
    - ```
      /system/bin/sh: can't find tty fd: No such device or address
      /system/bin/sh: warning: won't have full job control
      ```
5. Success! The library was loaded and executed, and netcat connected. The shell is a system shell with uid 1000
    - Some things to note:
        - Terminal escape sequences do not work in this shell, these are the arrow keys, delete, home

In some cases, the script may not connect to the netcat server. This is an issue that I haven't fully worked out yet. It can be fixed by retrying it a few times, or rebooting the device.

# How it works

1. First the TTS app has to be downgraded to a vulnerable version, as the newer versions have patched this vulnerability
2. The exploit app contains a service, which sends an intent that is normally sent after installing language packages
    - The intent provides an engine version to trick Samsung TTS into accepting it
    - It also contains an extra property `SMT_ENGINE_PATH` that leads to library contained within the exploit APK
4. Samsung TTS then loads the library provided in the intent
6. The library then starts a local netcat server as the system
