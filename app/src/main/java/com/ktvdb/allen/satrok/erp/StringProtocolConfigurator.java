package com.ktvdb.allen.satrok.erp;

import java.nio.charset.Charset;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.reactivex.netty.pipeline.PipelineConfigurator;

/**
 * Created by Allen on 15/10/13.
 */
public class StringProtocolConfigurator implements PipelineConfigurator<String, String>
{
    private final Charset inputCharset;

    public StringProtocolConfigurator()
    {
        this(Charset.forName("GBK"));
    }

    public StringProtocolConfigurator(Charset inputCharset)
    {
        this.inputCharset = inputCharset;
    }

    @Override
    public void configureNewPipeline(ChannelPipeline pipeline)
    {
        pipeline.addLast(new StringEncoder(inputCharset))
                .addLast(new StringDecoder(inputCharset));
    }
}
