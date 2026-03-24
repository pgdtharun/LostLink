package com.lostlink.app.data.repository

import com.lostlink.app.data.model.*
import java.util.Date
import java.util.Calendar

object MockDataRepository {
    
    private fun getDate(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }

    fun getLostandFoundItems(): List<Any> {
        return listOf(
            LostItem(
                id = "L001",
                title = "MacBook Air M2 - Silver",
                description = "Left in Computer Lab 03. Has a 'Code Is Life' sticker on the back. Very important for my project.",
                category = ItemCategory.ELECTRONICS,
                location = "Computer Lab 03",
                dateReported = getDate(1),
                images = listOf("https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=300"),
                status = ItemStatus.ACTIVE,
                reporterId = "user_nsbm_1",
                reporterName = "Dilshan Perera",
                contactInfo = "0771234567"
            ),
            FoundItem(
                id = "F001",
                title = "Commercial Bank Debit Card",
                description = "Found near the ATM at the main gate. The name on the card starts with 'K. Silva'.",
                category = ItemCategory.WALLETS,
                location = "Main Gate ATM",
                dateFound = getDate(0),
                images = listOf("https://images.unsplash.com/photo-1563013544-824ae1b90417?w=300"),
                status = ItemStatus.ACTIVE,
                finderId = "user_nsbm_2",
                finderName = "Ishara Fernando",
                contactInfo = "0719876543"
            ),
            LostItem(
                id = "L002",
                title = "Casio FX-991EX Calculator",
                description = "Scientific calculator used for the Engineering exam. Has my name 'Amara' written on the back with a marker.",
                category = ItemCategory.ELECTRONICS,
                location = "Engineering Block - Exam Hall",
                dateReported = getDate(2),
                images = listOf("https://images.unsplash.com/photo-1574607383476-f517f220d356?w=300"),
                status = ItemStatus.ACTIVE,
                reporterId = "user_nsbm_3",
                reporterName = "Amara Gunawardena",
                contactInfo = "0725556667"
            ),
            FoundItem(
                id = "F002",
                title = "Toyota Car Keys",
                description = "Set of keys with a leather Toyota keychain and a small Buddha statue.",
                category = ItemCategory.KEYS,
                location = "Cafeteria Parking",
                dateFound = getDate(1),
                images = listOf("https://images.unsplash.com/photo-1619641782822-ca3fb52f5db2?w=300"),
                status = ItemStatus.ACTIVE,
                finderId = "user_nsbm_4",
                finderName = "Security Office",
                contactInfo = "Counter 01"
            ),
            LostItem(
                id = "L003",
                title = "NSBM Student ID - 2024",
                description = "ID Number: 22105. Lost somewhere between the library and the main canteen.",
                category = ItemCategory.ID_CARDS,
                location = "Library Pathway",
                dateReported = getDate(3),
                images = listOf("https://images.unsplash.com/photo-1611532736597-de2d4265fba3?w=300"),
                status = ItemStatus.ACTIVE,
                reporterId = "user_nsbm_5",
                reporterName = "Kasun Wickrama",
                contactInfo = "0701112223"
            ),
            FoundItem(
                id = "F003",
                title = "Black Leather Wallet",
                description = "Contains some cash and a driving license. No student ID found inside.",
                category = ItemCategory.WALLETS,
                location = "Student Lounge",
                dateFound = getDate(0),
                images = listOf("https://images.unsplash.com/photo-1627123424574-724758594e93?w=300"),
                status = ItemStatus.ACTIVE,
                finderId = "user_nsbm_6",
                finderName = "Ruwan Siri",
                contactInfo = "0754445556"
            ),
            LostItem(
                id = "L004",
                title = "Blue Water Bottle - Milton",
                description = "Metal water bottle with some scratches at the bottom. Very emotional value.",
                category = ItemCategory.OTHER,
                location = "Open Air Theater",
                dateReported = getDate(4),
                images = listOf("https://images.unsplash.com/photo-1602143399827-bd95967c7c40?w=300"),
                status = ItemStatus.ACTIVE,
                reporterId = "user_nsbm_7",
                reporterName = "Sajith Madu",
                contactInfo = "0760001112"
            )
        )
    }
    
    fun getCurrentUser(): User {
        return User(
            id = "user_nsbm_logged",
            name = "Tharindu De Silva",
            email = "tharindu.d@nsbm.ac.lk",
            phone = "0778889990",
            avatarUrl = null,
            joinDate = getDate(100),
            idNumber = "NSBM/SOC/24/005",
            isVerified = true
        )
    }
    
    fun getUserMessages(): List<Message> {
        return listOf(
            Message(
                id = "M001",
                senderId = "user_nsbm_4",
                senderName = "Security Office",
                receiverId = "user_nsbm_logged",
                content = "Your keys have been handed over to the main security office. Please bring your ID to collect.",
                timestamp = getDate(0),
                itemId = "F002",
                isRead = false
            ),
            Message(
                id = "M002",
                senderId = "user_nsbm_6",
                senderName = "Ruwan Siri",
                receiverId = "user_nsbm_logged",
                content = "I found a wallet near the lounge. Is this yours?",
                timestamp = getDate(1),
                itemId = "F003",
                isRead = true
            )
        )
    }
}
