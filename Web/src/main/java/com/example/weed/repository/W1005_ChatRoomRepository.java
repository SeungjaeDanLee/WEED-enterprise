package com.example.weed.repository;

import com.example.weed.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface W1005_ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 추가적인 쿼리가 필요한 경우 여기에 작성할 수 있습니다.
}


