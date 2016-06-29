package dbService.dataSets;

import javax.persistence.*;

@Entity
@Table(name = "friends")
public class FriendsDataSet {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "friend_id")
    private long friendId;

    public FriendsDataSet(){

    }

    public FriendsDataSet(long id, long userId, long friend_id){
        this.id = id;
        this.userId = userId;
        this.friendId = friend_id;

    }

    public long getUserId() {
        return userId;
    }

    public long getFriendId() {
        return friendId;
    }
}
