package com.example.hearingtest.tone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class GenerationTone {
    private Integer baseFrequency = 441;
    private Integer countMiliseconds = 1000;
    private Integer volume = 20;
    private Integer samplingFrequency = 44100;
    private Boolean stereoChannel = true;              //true - left, false - right

    public GenerationTone(int baseFrequen, int countMilisec, int vol, boolean stCh) {
        this.baseFrequency = baseFrequen;
        this.countMiliseconds = countMilisec;
        this.volume = vol;
        this.stereoChannel = stCh;
    }

    public GenerationTone(){}

    public AudioTrack generateTone() {
        int count = (int)(countMiliseconds * samplingFrequency) / 1000;// & ~1;
        count = count * 2;
        short[] buf = new short[count];
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, samplingFrequency,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);

        for(int i = 0; i < count; i++){
            float angle = i / (float)(samplingFrequency / baseFrequency) * 2 * (float)Math.PI;
            if(stereoChannel){
                buf[i] = 0;
                i++;
                buf[i] = (short) (( (float) Math.sin(angle) * ((float) Math.pow(10, (volume / 20.0))) ) * Short.SIZE);
            } else{
                buf[i] = (short) (( (float) Math.sin(angle) * ((float) Math.pow(10, (volume / 20.0))) ) * Short.SIZE);
                i++;
                buf[i] = 0;
            }
        }
        track.write(buf,0, count);
        return track;
    }

    public void clearMemory(AudioTrack track) {
        try {
            track.pause();
        } catch (IllegalStateException e)
        {}
        track.flush();
        track.release();
    }

    public void setBaseFrequency(Integer base){
        this.baseFrequency = base;
    }

    public void setCountMiliseconds(Integer base){
        this.countMiliseconds = base;
    }

    public void setVolume(Integer base){
        this.volume = base;
    }

    public void setStereoChannel(Boolean base){
        this.stereoChannel = base;
    }

    public Integer getCountMiliseconds(){
        return countMiliseconds;
    }
}
