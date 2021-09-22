package tw.tcnr22.q0403;

public class q0403_Post {
    public String title;
    public String Description;
    public String cDate;

    public q0403_Post(String title, String description,String cDate) {

        this.title = title;//----------------------------------------品種名稱
        this.Description = description;//----------------------------簡述
        this.cDate = cDate;//-----------------------------------發佈更新日期
    }
}