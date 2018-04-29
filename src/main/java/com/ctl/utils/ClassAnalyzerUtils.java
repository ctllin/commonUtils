package com.ctl.utils;

import java.io.*;

public class ClassAnalyzerUtils {

    public static void main(String[] args) {
        DataInputStream input = null;
        try {
            input = new DataInputStream(ClassAnalyzerUtils.class.getResourceAsStream("ClassAnalyzerUtils.class"));
            analyze(input);
        }
        catch(Exception e) {
            System.out.println("Analyze failed!");
        }
        finally {
            try { input.close(); } catch(Exception e) {}
        }
    }

    public static void analyze(DataInputStream input) throws IOException {
        // read magic number:
        int magic = input.readInt();
        if(magic==0xCAFEBABE)
            System.out.println("magic number = 0xCAFEBABE");
        else
            throw new RuntimeException("Invalid magic number!");
        // read minor version and major version:
        short minor_ver = input.readShort();
        short major_ver = input.readShort();
        System.out.println("Version = " + major_ver + "." + minor_ver);
        // read constant pool:
        short const_pool_count = input.readShort();
        System.out.println("constant pool size = " + const_pool_count);
        // read each constant:
        for(int i=1; i<const_pool_count; i++) {//34 package
            analyzeConstant(input, i);
        }
    }

    public static void analyzeConstant(DataInputStream input, int index) throws IOException {
        byte flag = input.readByte();
        // for read:
        //byte n8;
        short n16;
        int n32;
        long n64;
        float f;
        double d;
        byte[] buffer;
        System.out.println("/nconst index = " + index + ", flag = " + (int)flag);
        switch(flag) {
        case 1: // utf-8 string
            System.out.println(" const type = Utf8");
            n16 = input.readShort();
            System.out.println("     length = " + n16);
            buffer = new byte[n16];
            input.readFully(buffer);
            System.out.println("      value = " + new String(buffer));
            break;
        case 3: // integer
            System.out.println(" const type = Integer");
            n32 = input.readInt();
            System.out.println("      value = " + n32);
            break;
        case 4: // float
            System.out.println(" const type = Float");
            f = input.readFloat();
            System.out.println("      value = " + f);
            break;
        case 5: // long
            System.out.println(" const type = Long");
            n64 = input.readLong();
            System.out.println("      value = " + n64);
            break;
        case 6: // double
            System.out.println(" const type = Double");
            d = input.readDouble();
            System.out.println("      value = " + d);
            break;
        case 7: // class or interface reference
            System.out.println(" const type = Class");
            n16 = input.readShort();
            System.out.println("      index = " + n16 + " (where to find the class name)");
            break;
        case 8: // string
            System.out.println(" const type = String");
            n16 = input.readShort();
            System.out.println("      index = " + n16);
            break;
        case 9: // field reference
            System.out.println(" const type = Fieldref");
            n16 = input.readShort();
            System.out.println("class index = " + n16 + " (where to find the class)");
            n16 = input.readShort();
            System.out.println("nameAndType = " + n16 + " (where to find the NameAndType)");
            break;
        case 10: // method reference
            System.out.println(" const type = Methodref");
            n16 = input.readShort();
            System.out.println("class index = " + n16 + " (where to find the class)");
            n16 = input.readShort();
            System.out.println("nameAndType = " + n16 + " (where to find the NameAndType)");
            break;
        case 11: // interface method reference
            System.out.println(" const type = InterfaceMethodref");
            n16 = input.readShort();
            System.out.println("class index = " + n16 + " (where to find the interface)");
            n16 = input.readShort();
            System.out.println("nameAndType = " + n16 + " (where to find the NameAndType)");
            break;
        case 12: // name and type reference
            System.out.println(" const type = NameAndType");
            n16 = input.readShort();
            System.out.println(" name index = " + n16 + " (where to find the name)");
            n16 = input.readShort();
            System.out.println(" descripter = " + n16 + " (where to find the descriptor)");
            break;
        default:
            throw new RuntimeException("Invalid constant pool flag: " + flag);
        }
    }
}