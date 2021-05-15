package editor.store;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import editor.io.OutputStream;


public final class Store {
	
	private Index[] indexes;
	private MainFile index255;
	private String path;
	private RandomAccessFile data;
	private byte[] readCachedBuffer;
	public Store(String path) throws IOException {
		this.path = path;
		data = new RandomAccessFile(path + "main_file_cache.dat2", "rw");
		readCachedBuffer = new byte[520];
		index255 = new MainFile(255, data, new RandomAccessFile(path + "main_file_cache.idx255", "rw"), readCachedBuffer);
		int idxsCount = index255.getArchivesCount();
		indexes = new Index[idxsCount];
		for(int id = 0; id < idxsCount; id++) {
			Index index = new Index(index255, new MainFile(id, data, new RandomAccessFile(path + "main_file_cache.idx"+id, "rw"), readCachedBuffer));
			if(index.getTable() == null)
				continue;
			indexes[id] = index;
		}
	}
	
	public byte[] generateIndex255Archive255() {
		OutputStream stream = new OutputStream(indexes.length * 8);
		for(int index = 0; index < indexes.length; index++) {
			if(indexes[index] == null) {
				stream.writeInt(0);
				stream.writeInt(0);
				continue;
			}
			stream.writeInt(indexes[index].getCRC());
			stream.writeInt(indexes[index].getTable().getRevision());
		}
		byte[] archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(archive, 0, archive.length);
		return archive;
	}
	
	public Index[] getIndexes() {
		return indexes;
	}
	
	public MainFile getIndex255() {
		return index255;
	}
	/*
	 * returns index
	 */
	public int addIndex(boolean named, boolean usesWhirpool, int tableCompression) throws IOException {
		int id = indexes.length;
		OutputStream stream = new OutputStream(4);
		stream.writeByte(5);
		stream.writeByte((named ? 0x1 : 0) | (usesWhirpool ? 0x2 : 0));
		stream.writeShort(0);
		byte[] archiveData = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(archiveData, 0, archiveData.length);
		Archive archive = new Archive(id, tableCompression, -1, archiveData);
		index255.putArchiveData(id, archive.compress());
		Index[] newIndexes = Arrays.copyOf(indexes, indexes.length+1);
		newIndexes[newIndexes.length-1] = new Index(index255, new MainFile(id, data, new RandomAccessFile(path + "main_file_cache.idx"+id, "rw")
		, readCachedBuffer));
		indexes = newIndexes;
		return id;
	}
	
}
