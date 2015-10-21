package com.ktvdb.allen.satrok.socket;


import com.google.gson.Gson;

/**
 * Created by Allen on 15/5/24.
 */
public class SocketMessageHandler
{
    public final static int CONNECTED        = 0;
    public final static int NEW_SONG_PUSH    = 1;
    public final static int NEW_SONG_INFO    = 2;
    public final static int ROOM_STATUS      = 3;
    public final static int SONG_STATUS      = 4;
    public final static int PLAY_LIST_ACTION = 5;
    public final static int DEVICE_ACTION    = 6;
    public final static int DB_VERSION       = 7;
    public final static int PLAY_ACTION      = 8;
    public final static int VIDEO_URL        = 9;

    private final Gson gson = new Gson();

//    public synchronized void handlerMessgae(IoSession session, String msg)
//    {
//        LogUtils.e(msg);
//        String[] s = msg.split("\r\n");
//        if (s.length > 2)
//        {
//            int cmd = Integer.parseInt(s[1]);
//            String content = s[2];
//            handler(cmd, content, session);
//        }
//    }
//
//    private void handler(int cmd, String content, IoSession session)
//    {
//        switch (cmd)
//        {
//            case ROOM_STATUS:
//                RoomStatusRequest roomStatusRequest = gson.fromJson(content,
//                                                                    RoomStatusRequest.class);
//                EventBus.getDefault().post(roomStatusRequest);
//                break;
//        }
//    }
}
