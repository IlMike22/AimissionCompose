package com.mind.market.aimissioncompose.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aimissionlite.models.domain.Genre
import com.mind.market.aimissioncompose.domain.models.Priority
import com.example.aimissionlite.models.domain.Status

@Entity(tableName = "goal_table")
data class GoalDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val creationDate: String,
    val changeDate: String,
    val isRepeated: Boolean,
    val genre: Genre,
    val status: Status,
    val priority: Priority
) {
    override fun equals(other: Any?): Boolean {
        if (other is GoalDto) {
            return this.id == other.id && this.genre == other.genre &&
                    this.priority == other.priority &&
                    this.title == other.title &&
                    this.description == other.description &&
                    this.isRepeated == other.isRepeated &&
                    this.creationDate == other.creationDate &&
                    this.status == other.status
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + changeDate.hashCode()
        result = 31 * result + isRepeated.hashCode()
        result = 31 * result + genre.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + priority.hashCode()
        return result
    }
}
