package com.franklin.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franklin.chatapp.entity.GroupChat;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {

}
