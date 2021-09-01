package bean;

import java.util.Arrays;

public enum CodingType {
    
    GOLOMB("Golomb", 0),
    ELIAS_GAMMA("Elias-Gamma", 1);
    
    private String name;
    private int header;
    
    private CodingType(String name, int header) {
        this.name = name;
        this.header = header;
    }
    
    public String getName() {
        return name;
    }

    public int getHeader() {
        return header;
    }
    
    public static CodingType findByName(final String name){
        return Arrays.stream(values())
                     .filter(value -> value.name.equals(name))
                     .findFirst().orElse(null);
    }

    public static CodingType findByHeader(final int header){
        return Arrays.stream(values())
                     .filter(value -> value.header == header)
                     .findFirst().orElse(null);
    }

}
