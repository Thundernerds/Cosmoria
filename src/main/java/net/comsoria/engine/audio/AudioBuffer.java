package net.comsoria.engine.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import net.comsoria.engine.libraryImplementation.AudioLibrary;
import net.comsoria.engine.loaders.FileLoader;
import org.lwjgl.stb.STBVorbisInfo;
import java.nio.ShortBuffer;
import static org.lwjgl.stb.STBVorbis.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.system.MemoryUtil.*;

public class AudioBuffer {
    private final int bufferId;
    private ShortBuffer pcm = null;
    private ByteBuffer vorbis = null;

    public AudioBuffer(String file) throws Exception {
        this.bufferId = AudioLibrary.AL.genBuffer();
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ShortBuffer pcm = readVorbis(file, 32 * 1024, info);

            // Copy to buffer
            AudioLibrary.AL.bufferData(bufferId, info.channels() == 1 ? AudioLibrary.Format.MONO_16 : AudioLibrary.Format.STEREO_16, pcm, info.sample_rate());
        }
    }

    public int getBufferId() {
        return this.bufferId;
    }

    public void cleanup() {
        AudioLibrary.AL.deleteBuffers(this.bufferId);
        if (pcm != null) {
            MemoryUtil.memFree(pcm);
        }
    }

    private ShortBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info) throws Exception {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            vorbis = FileLoader.ioResourceToByteBuffer(resource, bufferSize);
            IntBuffer error = stack.mallocInt(1);
            long decoder = stb_vorbis_open_memory(vorbis, error, null);
            if (decoder == NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }

            stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            pcm = MemoryUtil.memAllocShort(lengthSamples);

            pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            stb_vorbis_close(decoder);

            return pcm;
        }
    }
}