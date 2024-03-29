package com.mind.market.aimissioncompose.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.domain.models.Status

@Entity(tableName = "goal_table")
data class GoalDto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val creationDate: String = "",
    val changeDate: String = "",
    val isRepeated: Boolean = false,
    val genre: Genre = Genre.UNKNOWN,
    val status: Status = Status.UNKNOWN,
    val priority: Priority = Priority.UNKNOWN,
    @ColumnInfo(name = "finishDate", defaultValue = "")
    val finishDate: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (other is GoalDto) {
            return this.id == other.id && this.genre == other.genre &&
                    this.priority == other.priority &&
                    this.title == other.title &&
                    this.description == other.description &&
                    this.isRepeated == other.isRepeated &&
                    this.creationDate == other.creationDate &&
                    this.status == other.status &&
                    this.finishDate == other.finishDate
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
        result = 31 * result + finishDate.hashCode()
        return result
    }
}
