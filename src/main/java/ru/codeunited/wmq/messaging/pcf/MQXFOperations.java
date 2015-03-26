package ru.codeunited.wmq.messaging.pcf;

/**
 * This is a enum for MQIACF_OPERATION_ID
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.03.15.
 */
public enum MQXFOperations {
    MQXF_INIT(1),
    MQXF_TERM(2),
    MQXF_CONN(3),
    MQXF_CONNX(4),
    MQXF_DISC(5),
    MQXF_OPEN(6),
    MQXF_CLOSE(7),
    MQXF_PUT1(8),
    MQXF_PUT(9),
    MQXF_GET(10),
    MQXF_DATA_CONV_ON_GET(11),
    MQXF_INQ(12),
    MQXF_SET(13),
    MQXF_BEGIN(14),
    MQXF_CMIT(15),
    MQXF_BACK(16),
    MQXF_UNKNOWN(17),
    MQXF_STAT(18),
    MQXF_CB(19),
    MQXF_CTL(20),
    MQXF_CALLBACK(21),
    MQXF_SUB(22),
    MQXF_SUBRQ(23),
    MQXF_XACLOSE(24),
    MQXF_XACOMMIT(25),
    MQXF_XACOMPLETE(26),
    MQXF_XAEND(27),
    MQXF_XAFORGET(28),
    MQXF_XAOPEN(29),
    MQXF_XAPREPARE(30),
    MQXF_XARECOVER(31),
    MQXF_XAROLLBACK(32),
    MQXF_XASTART(33),
    MQXF_AXREG(34),
    MQXF_AXUNREG(35);

    final int code;

    MQXFOperations(int code) {
        this.code = code;
    }

    /**
     * Lookup for a enum of MQIACF_OPERATION_ID.
     * @param code
     * @return
     */
    public static MQXFOperations lookup(int code) {
        try {
            return values()[code - 1];
        } catch (ArrayIndexOutOfBoundsException out) {
            return MQXF_UNKNOWN;
        }
    }

    public boolean anyOf(MQXFOperations... operations) {
        for (MQXFOperations op : operations) {
            if (code() == op.code()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get operation code == MQIACF_OPERATION_ID
     * @return
     */
    public int code() {
        return this.code;
    }
}
