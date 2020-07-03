import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "incremetor")
    @GenericGenerator(name="incrementor",strategy = "increment")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name",unique =true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Column(name = "filename")
    private String filename;

    public String getFilename() {
        return filename;
    }
    public void setFileName(String name){
     this.filename=name;
    }



    @Column(name = "connecteed", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean connected;

    public Boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
    @Column(name = "DATA", unique = false, length = 100000)
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}

