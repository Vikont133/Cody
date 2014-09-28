package com.example.cody;

public interface VoiceHandler {
	void addUser(String name, short[] audioBuffer); 
	void removeUser(String name);
	void checkVoice(short[] audioBuffer);
}
