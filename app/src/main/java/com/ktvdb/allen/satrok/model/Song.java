package com.ktvdb.allen.satrok.model;


import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Allen on 15/7/21.
 */
public class Song extends NewokMedia implements Serializable
{

    private String tradName;

    public String getTradName()
    {
        return tradName;
    }

    public void setTradName(String tradName)
    {
        this.tradName = tradName;
    }

    private String enName;

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
    }

    private String jpnName;

    public String getJpnName()
    {
        return jpnName;
    }

    public void setJpnName(String jpnName)
    {
        this.jpnName = jpnName;
    }

    private String korName;

    public String getKorName()
    {
        return korName;
    }

    public void setKorName(String korName)
    {
        this.korName = korName;
    }

    private Integer wordCount;

    public Integer getWordCount()
    {
        return wordCount;
    }

    public void setWordCount(Integer wordCount)
    {
        this.wordCount = wordCount;
    }

    private String namesimplicity;

    public String getNamesimplicity()
    {
        return namesimplicity;
    }

    public void setNamesimplicity(String namesimplicity)
    {
        this.namesimplicity = namesimplicity;
    }

    private String stroke;

    public String getStroke()
    {
        return stroke;
    }

    public void setStroke(String stroke)
    {
        this.stroke = stroke;
    }


    private Album album;

    public Album getAlbum()
    {
        return album;
    }

    public void setAlbum(Album album)
    {
        this.album = album;
    }

    /**
     * 语种
     */
    private SysDictionary language;

    public SysDictionary getLanguage()
    {
        return language;
    }

    public void setLanguage(SysDictionary language)
    {
        this.language = language;
    }


    /**
     * 音色分类
     */
    private SysDictionary songType;

    public SysDictionary getSongType()
    {
        return songType;
    }

    public void setSongType(SysDictionary songType)
    {
        this.songType = songType;
    }

    /**
     * 所属年代
     */
    private SysDictionary belongToPeriod;

    public SysDictionary getBelongToPeriod()
    {
        return belongToPeriod;
    }

    public void setBelongToPeriod(SysDictionary belongToPeriod)
    {
        this.belongToPeriod = belongToPeriod;
    }

    private String img;

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    private Timestamp launchDate;

    public Timestamp getLaunchDate()
    {
        return launchDate;
    }

    public void setLaunchDate(Timestamp launchDate)
    {
        this.launchDate = launchDate;
    }


    /**
     * 词作者
     */
    private String lyricist;

    public String getLyricist()
    {
        return lyricist;
    }

    public void setLyricist(String lyricist)
    {
        this.lyricist = lyricist;
    }

    private String compose;

    public String getCompose()
    {
        return compose;
    }

    public void setCompose(String compose)
    {
        this.compose = compose;
    }

    private String repairMark;

    public String getRepairMark()
    {
        return repairMark;
    }

    public void setRepairMark(String repairMark)
    {
        this.repairMark = repairMark;
    }

    private String publisher;

    public String getPublisher()
    {
        return publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    private String selfCreation;

    public String getSelfCreation()
    {
        return selfCreation;
    }

    public void setSelfCreation(String selfCreation)
    {
        this.selfCreation = selfCreation;
    }

    private String adContent;

    public String getAdContent()
    {
        return adContent;
    }

    public void setAdContent(String adContent)
    {
        this.adContent = adContent;
    }

    private Integer volume;

    public Integer getVolume()
    {
        return volume;
    }

    public void setVolume(Integer volume)
    {
        this.volume = volume;
    }

    private Integer originalSoundChannel;

    public Integer getOriginalSoundChannel()
    {
        return originalSoundChannel;
    }

    public void setOriginalSoundChannel(Integer originalSoundChannel)
    {
        this.originalSoundChannel = originalSoundChannel;
    }

    private Integer accompanySoundChannel;

    public Integer getAccompanySoundChannel()
    {
        return accompanySoundChannel;
    }

    public void setAccompanySoundChannel(Integer accompanySoundChannel)
    {
        this.accompanySoundChannel = accompanySoundChannel;
    }

    private Integer audioTrack;

    public Integer getAudioTrack()
    {
        return audioTrack;
    }

    public void setAudioTrack(Integer audioTrack)
    {
        this.audioTrack = audioTrack;
    }


    private String songResolution;

    public String getSongResolution()
    {
        return songResolution;
    }

    public void setSongResolution(String songResolution)
    {
        this.songResolution = songResolution;
    }

    private Integer is51Channel;

    public Integer getIs51Channel()
    {
        return is51Channel;
    }

    public void setIs51Channel(Integer is51Channel)
    {
        this.is51Channel = is51Channel;
    }

    private Integer isGradeLib;

    public Integer getIsGradeLib()
    {
        return isGradeLib;
    }

    public void setIsGradeLib(Integer isGradeLib)
    {
        this.isGradeLib = isGradeLib;
    }

    private Integer isOnlinePk;

    public Integer getIsOnlinePk()
    {
        return isOnlinePk;
    }

    public void setIsOnlinePk(Integer isOnlinePk)
    {
        this.isOnlinePk = isOnlinePk;
    }

    private String lyric;

    public String getLyric()
    {
        return lyric;
    }

    public void setLyric(String lyric)
    {
        this.lyric = lyric;
    }

    private String lyricFile;

    public String getLyricFile()
    {
        return lyricFile;
    }

    public void setLyricFile(String lyricFile)
    {
        this.lyricFile = lyricFile;
    }

    private String songFileName;

    public String getSongFileName()
    {
        return songFileName;
    }

    public void setSongFileName(String songFileName)
    {
        this.songFileName = songFileName;
    }

    private String songFilePath;

    public String getSongFilePath()
    {
        return songFilePath;
    }

    public void setSongFilePath(String songFilePath)
    {
        this.songFilePath = songFilePath;
    }

    private int status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    private int hot;

    public int getHot()
    {
        return hot;
    }

    public void setHot(int hot)
    {
        this.hot = hot;
    }


    /**
     * 歌星名称
     */
    private String singerNames;

    /**
     * @return
     */
    public String getSingerNames()
    {
        return singerNames;
    }

    public void setSingerNames(String singerNames)
    {
        this.singerNames = singerNames;
    }

    /**
     * 是否是偶数行
     */
    private boolean even;

    public boolean isEven()
    {
        return even;
    }

    public void setEven(boolean even)
    {
        this.even = even;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        if (status != song.status) return false;
        if (id != null ? !id.equals(song.id) : song.id != null) return false;
        if (tradName != null ? !tradName.equals(song.tradName) : song.tradName != null)
        {
            return false;
        }
        if (enName != null ? !enName.equals(song.enName) : song.enName != null) return false;
        if (jpnName != null ? !jpnName.equals(song.jpnName) : song.jpnName != null) return false;
        if (korName != null ? !korName.equals(song.korName) : song.korName != null) return false;
        if (wordCount != null ? !wordCount.equals(song.wordCount) : song.wordCount != null)
        {
            return false;
        }
        if (namesimplicity != null ? !namesimplicity.equals(song.namesimplicity)
                : song.namesimplicity != null)
        {
            return false;
        }
        if (stroke != null ? !stroke.equals(song.stroke) : song.stroke != null) return false;
        if (img != null ? !img.equals(song.img) : song.img != null) return false;
        if (launchDate != null ? !launchDate.equals(song.launchDate) : song.launchDate != null)
        {
            return false;
        }
        if (lyricist != null ? !lyricist.equals(song.lyricist) : song.lyricist != null)
        {
            return false;
        }
        if (compose != null ? !compose.equals(song.compose) : song.compose != null) return false;
        if (repairMark != null ? !repairMark.equals(song.repairMark) : song.repairMark != null)
        {
            return false;
        }
        if (publisher != null ? !publisher.equals(song.publisher) : song.publisher != null)
        {
            return false;
        }
        if (selfCreation != null ? !selfCreation.equals(song.selfCreation)
                : song.selfCreation != null)
        {
            return false;
        }
        if (adContent != null ? !adContent.equals(song.adContent) : song.adContent != null)
        {
            return false;
        }
        if (volume != null ? !volume.equals(song.volume) : song.volume != null) return false;
        if (originalSoundChannel != null ? !originalSoundChannel.equals(song.originalSoundChannel)
                : song.originalSoundChannel != null)
        {
            return false;
        }
        if (accompanySoundChannel != null
                ? !accompanySoundChannel.equals(song.accompanySoundChannel)
                : song.accompanySoundChannel != null)
        {
            return false;
        }
        if (audioTrack != null ? !audioTrack.equals(song.audioTrack) : song.audioTrack != null)
        {
            return false;
        }
        if (songResolution != null ? !songResolution.equals(song.songResolution)
                : song.songResolution != null)
        {
            return false;
        }
        if (is51Channel != null ? !is51Channel.equals(song.is51Channel) : song.is51Channel != null)
        {
            return false;
        }
        if (isGradeLib != null ? !isGradeLib.equals(song.isGradeLib) : song.isGradeLib != null)
        {
            return false;
        }
        if (isOnlinePk != null ? !isOnlinePk.equals(song.isOnlinePk) : song.isOnlinePk != null)
        {
            return false;
        }
        if (lyric != null ? !lyric.equals(song.lyric) : song.lyric != null) return false;
        if (lyricFile != null ? !lyricFile.equals(song.lyricFile) : song.lyricFile != null)
        {
            return false;
        }
        if (songFileName != null ? !songFileName.equals(song.songFileName)
                : song.songFileName != null)
        {
            return false;
        }
        if (songFilePath != null ? !songFilePath.equals(song.songFilePath)
                : song.songFilePath != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tradName != null ? tradName.hashCode() : 0);
        result = 31 * result + (enName != null ? enName.hashCode() : 0);
        result = 31 * result + (jpnName != null ? jpnName.hashCode() : 0);
        result = 31 * result + (korName != null ? korName.hashCode() : 0);
        result = 31 * result + (wordCount != null ? wordCount.hashCode() : 0);
        result = 31 * result + (namesimplicity != null ? namesimplicity.hashCode() : 0);
        result = 31 * result + (stroke != null ? stroke.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + (launchDate != null ? launchDate.hashCode() : 0);
        result = 31 * result + (lyricist != null ? lyricist.hashCode() : 0);
        result = 31 * result + (compose != null ? compose.hashCode() : 0);
        result = 31 * result + (repairMark != null ? repairMark.hashCode() : 0);
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (selfCreation != null ? selfCreation.hashCode() : 0);
        result = 31 * result + (adContent != null ? adContent.hashCode() : 0);
        result = 31 * result + (volume != null ? volume.hashCode() : 0);
        result = 31 * result + (originalSoundChannel != null ? originalSoundChannel.hashCode() : 0);
        result = 31 * result +
                (accompanySoundChannel != null ? accompanySoundChannel.hashCode() : 0);
        result = 31 * result + (audioTrack != null ? audioTrack.hashCode() : 0);
        result = 31 * result + (songResolution != null ? songResolution.hashCode() : 0);
        result = 31 * result + (is51Channel != null ? is51Channel.hashCode() : 0);
        result = 31 * result + (isGradeLib != null ? isGradeLib.hashCode() : 0);
        result = 31 * result + (isOnlinePk != null ? isOnlinePk.hashCode() : 0);
        result = 31 * result + (lyric != null ? lyric.hashCode() : 0);
        result = 31 * result + (lyricFile != null ? lyricFile.hashCode() : 0);
        result = 31 * result + (songFileName != null ? songFileName.hashCode() : 0);
        result = 31 * result + (songFilePath != null ? songFilePath.hashCode() : 0);
        result = 31 * result + status;
        return result;
    }
}
