package editor.store;

import editor.io.InputStream;
import editor.io.OutputStream;
import editor.util.bzip2.BZip2Decompressor;
import editor.util.crc32.CRC32HGenerator;
import editor.util.gzip.GZipCompressor;
import editor.util.gzip.GZipDecompressor;
import editor.utils.Constants;

public class Archive {
	
	private int id;
	private int revision;
	private int compression;
	private byte[] data;
	private int[] keys;
	
	protected Archive(int id, byte[] archive, int[] keys) {
		this.id = id;
		this.keys = keys;
		decompress(archive);
		
	}
	
	public Archive(int id, int compression, int revision, byte[] data) {
		this.id = id;
		this.compression = compression;
		this.revision = revision;
		this.data = data;
	}
	
	public byte[] compress() {
		OutputStream stream = new OutputStream();
		stream.writeByte(compression);
		byte[] compressedData;
		switch(compression) {
		case Constants.NO_COMPRESSION: //no compression
			compressedData = data;
			stream.writeInt(data.length);
		break;
		case Constants.BZIP2_COMPRESSION: 
			compressedData = null; //TODO
		break;
		default: //gzip
			compressedData = GZipCompressor.compress(data);
			stream.writeInt(compressedData.length);
			stream.writeInt(data.length);
		break;
		}
		stream.writeBytes(compressedData);
		byte[] compressed = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(compressed, 0, compressed.length);
		if(revision != -1) {
			compressed[compressed.length - 2] = (byte) (revision >> 8);
			compressed[compressed.length - 1] = (byte) revision;
		}
		return compressed;
	}
	
	private void decompress(byte[] archive) {
		InputStream stream = new InputStream(archive);
		if(keys != null && keys.length != 0)
			stream.decodeXTEA(keys);
		compression = stream.readUnsignedByte();
		int compressedLength = stream.readInt();
		if(compressedLength < 0 || compressedLength > Constants.MAX_VALID_ARCHIVE_LENGTH)
			throw new RuntimeException("INVALID ARCHIVE HEADER");
		switch(compression) {
		case Constants.NO_COMPRESSION: //no compression
			data = new byte[compressedLength];
			checkRevision(stream, compressedLength);
			stream.readBytes(data, 0, compressedLength);
		break;
		case Constants.BZIP2_COMPRESSION: //bzip2
			int length = stream.readInt();
			if(length <= 0 || length > 1000000000) {
				data = null;
				break;
			}
			data = new byte[length];
			checkRevision(stream, compressedLength);
			BZip2Decompressor.decompress(data, archive, compressedLength, 9);
		break;
		default: //gzip
			length = stream.readInt();
			if(length <= 0 || length > 1000000000) {
				data = null;
				break;
			}
			data = new byte[length];
			checkRevision(stream, compressedLength);
			GZipDecompressor.decompress(stream, data);
		break;
		}
	}
	
	private void checkRevision(InputStream stream, int compressedLength) {
		int offset = stream.getOffset();
		if(stream.getLength()- (compressedLength+stream.getOffset()) >= 2) {
			stream.setOffset(stream.getLength()-2);
			revision = stream.readUnsignedShort();
			stream.setOffset(offset);
		}else
			revision = -1;
		
	}
	
	public int editNoRevision(byte[] data, MainFile mainFile) {
		this.data = data;
		if(compression == Constants.BZIP2_COMPRESSION)
			compression = Constants.GZIP_COMPRESSION;
		byte[] compressed = compress();
		if(!mainFile.putArchiveData(id, compressed))
			return -1;
		return CRC32HGenerator.getHash(compressed);
	}
	
	public int getId() {
		return id;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public int getDecompressedLength() {
		return data.length;
	}
	
	public int getRevision() {
		return revision;
	}
	
	public int getCompression() {
		return compression;
	}
	
	public int[] getKeys() {
		return keys;
	}
	
}
