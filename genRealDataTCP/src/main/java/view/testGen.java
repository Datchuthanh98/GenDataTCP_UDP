package view;

import net.andreinc.mockneat.MockNeat;
import net.andreinc.mockneat.types.enums.IPv4Type;

import java.util.List;
import java.util.Random;

import static net.andreinc.mockneat.types.enums.IPv4Type.CLASS_A;

public class testGen {


    public static void main(String[] args) {

        MockNeat mock = MockNeat.threadLocal();

        String ipv4 = mock.ipv4s().val();
        System.out.println(ipv4);

        String ipClassA = mock.ipv4s().type(CLASS_A).val();
        System.out.println(ipClassA);

        String classAorB = mock.ipv4s().types(CLASS_A, IPv4Type.CLASS_B).val();
        System.out.println(classAorB);

        List<String> ip4s = mock.ipv4s().list(10).val();
        System.out.println(ip4s);
    }
}
