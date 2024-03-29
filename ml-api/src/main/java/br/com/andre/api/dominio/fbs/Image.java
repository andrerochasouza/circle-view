package br.com.andre.api.dominio.fbs;// automatically generated by the FlatBuffers compiler, do not modify

import com.google.flatbuffers.BaseVector;
import com.google.flatbuffers.ByteVector;
import com.google.flatbuffers.Constants;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public final class Image extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_1_12_0(); }
  public static Image getRootAsImage(ByteBuffer _bb) { return getRootAsImage(_bb, new Image()); }
  public static Image getRootAsImage(ByteBuffer _bb, Image obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Image __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int data(int j) { int o = __offset(4); return o != 0 ? bb.get(__vector(o) + j * 1) & 0xFF : 0; }
  public int dataLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public ByteVector dataVector() { return dataVector(new ByteVector()); }
  public ByteVector dataVector(ByteVector obj) { int o = __offset(4); return o != 0 ? obj.__assign(__vector(o), bb) : null; }
  public ByteBuffer dataAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer dataInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public boolean mutateData(int j, int data) { int o = __offset(4); if (o != 0) { bb.put(__vector(o) + j * 1, (byte) data); return true; } else { return false; } }

  public static int createImage(FlatBufferBuilder builder,
      int dataOffset) {
    builder.startTable(1);
    Image.addData(builder, dataOffset);
    return Image.endImage(builder);
  }

  public static void startImage(FlatBufferBuilder builder) { builder.startTable(1); }
  public static void addData(FlatBufferBuilder builder, int dataOffset) { builder.addOffset(0, dataOffset, 0); }
  public static int createDataVector(FlatBufferBuilder builder, byte[] data) { return builder.createByteVector(data); }
  public static int createDataVector(FlatBufferBuilder builder, ByteBuffer data) { return builder.createByteVector(data); }
  public static void startDataVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static int endImage(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }
  public static void finishImageBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
  public static void finishSizePrefixedImageBuffer(FlatBufferBuilder builder, int offset) { builder.finishSizePrefixed(offset); }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public Image get(int j) { return get(new Image(), j); }
    public Image get(Image obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

