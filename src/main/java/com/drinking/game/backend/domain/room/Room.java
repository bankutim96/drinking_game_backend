package com.drinking.game.backend.domain.room;

import com.drinking.game.backend.domain.EntityBase;
import com.drinking.game.backend.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Room extends EntityBase implements Serializable {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Enumerated(EnumType.STRING)
    private RoomState state;

    @Column(nullable = false, unique = true)
    private String joinCode;

    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "currentRoom", fetch = FetchType.LAZY)
    private List<User> joinedUsers;

    @OneToOne(fetch = FetchType.LAZY)
    private User adminUser;

}
