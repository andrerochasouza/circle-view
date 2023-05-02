package br.com.andre.api.dominio;

public enum TypeImage {
    JPG(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}),
    PNG(new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E}),
    JPEG(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}),
    BMP(new byte[]{(byte) 0x42, (byte) 0x4D});

    private byte[] header;

    TypeImage(byte[] header) {
        this.header = header;
    }

    public byte[] getHeader() {
        return header;
    }

    public static TypeImage fromBytes(byte[] bytes) {
        for (TypeImage type : TypeImage.values()) {
            byte[] header = type.getHeader();
            boolean match = true;
            for (int i = 0; i < header.length; i++) {
                if (header[i] != bytes[i]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return type;
            }
        }
        throw new RuntimeException("Tipo de imagem não suportado!");
    }

    public String getExtension() {
        switch (this) {
            case JPG:
            case JPEG:
                return "jpg";
            case PNG:
                return "png";
            case BMP:
                return "bmp";
            default:
                throw new RuntimeException("Tipo de imagem não suportado!");
        }
    }
}
