package com.buoobuoo.mesuite.meutils.unicode;

public class UnicodeSpaceUtil {

    public static String getSpace(int pixel){
        return pixel < 0 ? getNeg(pixel) : getPos(pixel);
    }

    public static String getNeg(int pixel) {
        pixel = Math.abs(pixel);
        String binary = new StringBuilder(Integer.toBinaryString(pixel)).reverse().toString();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(char c : binary.toCharArray()){
            if(c != '0')
            {
                sb.append(NegativeChar.getCharByWeight((int)Math.pow(2, index)).s );
            }
            index++;
        }

        return sb.toString();
    }

    public static String getPos(int pixel) {
        String binary = new StringBuilder(Integer.toBinaryString(pixel)).reverse().toString();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(char c : binary.toCharArray()){
            if(c != '0')
            {
                sb.append(PositiveChar.getCharByWeight((int)Math.pow(2, index)).s );
            }
            index++;
        }

        return sb.toString();
    }


    private enum NegativeChar{
        NEG1(1, CharRepo.NEG1),
        NEG2(2, CharRepo.NEG2),
        NEG4(4, CharRepo.NEG4),
        NEG8(8, CharRepo.NEG8),
        NEG16(16, CharRepo.NEG16),
        NEG32(32, CharRepo.NEG32),
        NEG64(64, CharRepo.NEG64),
        NEG128(128, CharRepo.NEG128),
        NEG256(256, CharRepo.NEG256),
        NEG512(512, CharRepo.NEG512),
        NEG1024(1024, CharRepo.NEG1024);

        private int weight;
        private CharRepo s;
        NegativeChar(int weight, CharRepo s){
            this.weight = weight;
            this.s = s;
        }

        static NegativeChar getCharByWeight(int weight)
        {
            for(NegativeChar c : NegativeChar.values())
                if(c.weight==weight)
                    return c;
            return null;
        }
    }

    private enum PositiveChar{
        POS1(1, CharRepo.POS1),
        POS2(2, CharRepo.POS2),
        POS4(4, CharRepo.POS4),
        POS8(8, CharRepo.POS8),
        POS16(16, CharRepo.POS16),
        POS32(32, CharRepo.POS32),
        POS64(64, CharRepo.POS64),
        POS128(128, CharRepo.POS128),
        POS256(256, CharRepo.POS256),
        POS512(512, CharRepo.POS512),
        POS1024(1024, CharRepo.POS1024);

        private int weight;
        private CharRepo s;
        PositiveChar(int weight, CharRepo s){
            this.weight = weight;
            this.s = s;
        }

        static PositiveChar getCharByWeight(int weight)
        {
            for(PositiveChar c : PositiveChar.values())
                if(c.weight==weight)
                    return c;
            return null;
        }
    }
}
