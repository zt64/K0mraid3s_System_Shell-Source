# K0mraid3s System Shell
As seen on XDA https://forum.xda-developers.com/t/system-shell-exploit-all-samsung-mobile-devices-no-bl-unlock-required.4543071/

# Notice

Currently there is only a vulnerable APK for arm64 devices with an existing SMT with SHA-1 checksum: `9ca5170f381919dfe0446fcdab18b19a143b3163`
So unfortunately other devices are currently unsupported. If a vulnerable APK for your device is found, it will be added to the repo.

# Usage

### Prerequisites
- ADB installed and added to the system PATH variable
- Samsung text-to-speech engine battery optimization set to unrestricted

### Steps
1. Install the `app-release.apk` in the latest [release](https://github.com/zt64/K0mraid3s_System_Shell-Source/releases/latest)
2. Reboot the device
3. Run the script in `assets`
    - If you're on Linux this is `exploit.sh`
    - If you're on Windows this is `exploit.bat`
4. If all goes well, the script should say:
    - ```
      /system/bin/sh: can't find tty fd: No such device or address
      /system/bin/sh: warning: won't have full job control
      ```
5. Success! The library was loaded and executed, and netcat connected. The shell is a system shell with uid 1000
    - Some things to note:
        - Terminal escape sequences do not work in this shell, these are the arrow keys, delete, home
        - The device may say that `Samsung text-to-speech engine keeps stopping`, this is normal.
         **Do not** click anywhere on the screen, as this will force stop the engine, killing the connection.
         Instead, click app info and from there you can go to the homescreen.

In some cases, the script may not connect to the netcat server. This is an issue that I haven't fully worked out yet. It can be fixed by retrying it a few times, or rebooting the device.

# How it works

1. First the TTS app has to be downgraded to a vulnerable version, as the newer versions have patched this vulnerability
2. The exploit app contains a receiver, which can be triggered via ADB to send an intent to the TTS app
    - The intent provides an engine version to trick Samsung TTS into accepting it
    - It also contains an extra property `SMT_ENGINE_PATH` that leads to library contained within the exploit APK
3. Samsung TTS then loads the library provided in the intent
4. The library then starts a local netcat server as the system
