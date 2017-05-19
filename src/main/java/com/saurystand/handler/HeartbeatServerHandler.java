package com.saurystand.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * 心跳服务的处理器
 * Created by Administrator on 2017/5/19.
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

    // Return a unreleasable view on the given ByteBuf
    // which will just ignore release and retain calls.
    private static ByteBuf  HEARTBEAT_SEQUEUE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("I am heartRate Buffer", CharsetUtil.UTF_8));

    /**
     * 略微简单的一个事件触发器，多多学习
     * @param channelHandlerContext
     * @param object
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext,Object object)throws Exception{
        //.instanceof运算符能够用来判断一个对象是否为:一个类的实例，一个实现指定接口的类的实例，一个子类的实例
        if(object instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) object;
            String type="";

            if(event.state() == IdleState.READER_IDLE){
                type="Read idle";
            }else if(event.state() == IdleState.WRITER_IDLE){
                type="Write idle";
            }else if(event.state() == IdleState.ALL_IDLE){
                type="All idle";
            }
            channelHandlerContext.writeAndFlush(HEARTBEAT_SEQUEUE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            System.out.println(channelHandlerContext.channel().remoteAddress()+" overtime type is "+type);

        }else{
            super.userEventTriggered(channelHandlerContext,object);
        }
    }



}
