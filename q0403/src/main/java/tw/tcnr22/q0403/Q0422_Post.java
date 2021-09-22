package tw.tcnr22.q0403;

public class Q0422_Post {

    public String 電話1;
    public String 育苗場名稱;
    public String 服務項目;
    public String 簡介;
    public String 地址;

    public Q0422_Post(String 育苗場名稱, String 服務項目, String 簡介, String 地址, String 電話1) {

        this.育苗場名稱 = 育苗場名稱;//----------------------------------------育苗場名稱
        this.服務項目 = 服務項目;//----------------------------服務項目
        this.簡介 = 簡介;//-----------------------------------簡介
        this.地址 = 地址;//-----------------------------------地址
        this.電話1 = 電話1;//-----------------------------------電話1


    }

    public Q0422_Post(String b_地址, String b_電話1) {
        this.地址 = b_地址;  //住址
        this.電話1 = b_電話1;  //電話1

    }
}