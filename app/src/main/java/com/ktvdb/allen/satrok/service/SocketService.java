package com.ktvdb.allen.satrok.service;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.socket.model.Message;
import com.ktvdb.allen.satrok.socket.model.ReceiveMessage;
import com.ktvdb.allen.satrok.utils.GsonUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

/**
 * Created by Allen on 15/9/29.
 */
public class SocketService extends SimpleChannelInboundHandler<DatagramPacket>
{
    final Bootstrap      bootstrap;
    final EventLoopGroup mGroup;
    final MessageEncoder encoder;

    private Channel ch;

    public static final String DEVICE_INIT = "device_init";

    public SocketService()
    {
        EventBus.getDefault().register(this);
        encoder = new MessageEncoder();
        bootstrap = new Bootstrap();
        mGroup = new NioEventLoopGroup();
        bootstrap.channel(NioDatagramChannel.class)
                 .group(mGroup)
                 .option(ChannelOption.SO_BROADCAST, true)
                 .handler(new ChannelInitializer<Channel>()
                 {
                     @Override
                     protected void initChannel(Channel ch) throws Exception
                     {
                         ch.pipeline().addLast(SocketService.this);
                         ch.pipeline().addLast(encoder);
                     }
                 });
        try
        {
            ch = bootstrap.bind(0).sync().channel();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception
    {
        String         json    = msg.content().toString(CharsetUtil.UTF_8);
        ReceiveMessage receive = GsonUtil.gson().fromJson(json, ReceiveMessage.class);
        if (receive.cmd.equals(DEVICE_INIT))
        {
            receive.ext.put("serverIp", msg.sender().getAddress().getHostAddress());
            receive.ext.put("erpIp", msg.sender().getAddress().getHostAddress());
            encoder.setServerAddres(msg.sender());
        }
        EventBus.getDefault().post(receive, receive.cmd);
        LogUtils.e(receive);
    }

    public void init()
    {
        Message<String> message = new Message<>();
        message.setCmd(DEVICE_INIT);
        message.setContent(StarokApplication.getAppContext().uuid());
        ch.writeAndFlush(message);
    }

    public static class MessageEncoder extends MessageToMessageEncoder<Message>
    {
        InetSocketAddress serverAddres = new InetSocketAddress("255.255.255.255", 53000);

        public InetSocketAddress getServerAddres()
        {
            return serverAddres;
        }

        public void setServerAddres(InetSocketAddress serverAddres)
        {
            this.serverAddres = serverAddres;
        }

        @Override
        protected void encode(ChannelHandlerContext ctx,
                              Message msg,
                              List<Object> out) throws Exception
        {
            String s = GsonUtil.gson().toJson(msg);
            DatagramPacket packet = new DatagramPacket(ctx.alloc().buffer().writeBytes(s.getBytes(
                    CharsetUtil.UTF_8)), serverAddres);
            out.add(packet);
        }
    }

    @Subscriber(tag = "sendMessage",
                mode = ThreadMode.ASYNC)
    void sendMessage(Message message)
    {
        ch.writeAndFlush(message);
    }


}
