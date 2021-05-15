package editor.util.rsmv;


public final class Buffer {
   private byte[] buffer;
   public int offset;

   public Buffer(byte[] data) {
      this.buffer = data;
      this.offset = 0;
   }

   public final int readUnsignedByte() {
      return this.buffer[this.offset++] & 255;
   }

   public final byte readByte() {
      return this.buffer[this.offset++];
   }

   public final int readUnsignedShort() {
      this.offset += 2;
      return ((this.buffer[this.offset - 2] & 255) << 8) + (this.buffer[this.offset - 1] & 255);
   }

   public final int readMid() {
      this.offset += 3;
      return ((this.buffer[this.offset - 3] & 255) << 16) + ((this.buffer[this.offset - 2] & 255) << 8) + (this.buffer[this.offset - 1] & 255);
   }

   public final int readShortv2() {
      this.offset += 2;
      return ((this.buffer[this.offset - 2] & 255) << 8) + (this.buffer[this.offset - 1] & 255);
   }

   public final int readShortSmart() {
      return (this.buffer[this.offset] & 255) < 128 ? this.readUnsignedByte() - 64 : this.readUnsignedShort() - 49152;
   }
}
