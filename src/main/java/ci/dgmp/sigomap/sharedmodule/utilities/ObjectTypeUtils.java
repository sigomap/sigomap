package ci.dgmp.sigomap.sharedmodule.utilities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class ObjectTypeUtils
{
    public static boolean isSimpleObject(Object obj)
    {
        return obj == null || obj.getClass().isPrimitive() ||
                obj instanceof Integer ||
                obj instanceof Double ||
                obj instanceof Long ||
                obj instanceof Short ||
                obj instanceof Byte ||
                obj instanceof Character ||
                obj instanceof Boolean ||
                obj instanceof Float ||
                obj instanceof BigInteger ||
                obj instanceof BigDecimal ||
                obj instanceof String ||
                obj instanceof Date ||
                obj instanceof LocalDate ||
                obj instanceof LocalDateTime;

    }

    public static void main(String[] args) {
        System.out.println("1 est type simple : " + isSimpleObject(1));
    }
}
