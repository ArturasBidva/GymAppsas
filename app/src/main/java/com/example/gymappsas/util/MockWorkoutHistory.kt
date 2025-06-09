
import com.example.gymappsas.data.db.models.completedworkout.CompletedWorkout
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.util.MockExerciseData
import java.time.LocalDate

object MockWorkoutHistory {

    private val mockExercise1 = Exercise(
        id = 1L,
        name = "Bench Press",
        images = listOf(),
        instructions = listOf("gg","haha"),
        primaryMuscles = listOf("Biceps")
    )

    private val mockExercise2 = Exercise(
        id = 2L,
        name = "Squats",
        images = listOf(),
        instructions = listOf("gg","haha"),
        primaryMuscles = listOf("Biceps")
    )

    private val mockExercise3 = Exercise(
        id = 3L,
        name = "Deadlift",
        images = listOf(),
        instructions = listOf("gg","haha"),
        primaryMuscles = listOf("Biceps")
    )

    private val mockExerciseWorkout1 = ExerciseWorkout(
        id = 1L,
        completedCount = 10,
        weight = 80,
        goal = 10,
        exercise = MockExerciseData.mockExercises[0]
    )

    private val mockExerciseWorkout2 = ExerciseWorkout(
        id = 2L,
        completedCount = 12,
        weight = 100,
        goal = 12,
        exercise = MockExerciseData.mockExercises[2]
    )

    private val mockExerciseWorkout3 = ExerciseWorkout(
        id = 3L,
        completedCount = 8,
        weight = 120,
        goal = 8,
        exercise = MockExerciseData.mockExercises[1]
    )

    private val mockWorkout1 = CompletedWorkout(
        id = 1L,
        workout = Workout(
            title = "Full Body Workout",
            exerciseWorkouts = listOf(mockExerciseWorkout1, mockExerciseWorkout2, mockExerciseWorkout3)
        ),
        completedDate = LocalDate.now().minusDays(1)
    )

    private val mockWorkout2 = CompletedWorkout(
        id = 2L,
        workout = Workout(
            title = "Leg Day",
            exerciseWorkouts = listOf(mockExerciseWorkout2, mockExerciseWorkout3)
        ),
        completedDate = LocalDate.now().minusDays(2)
    )

    private val mockWorkout3 = CompletedWorkout(
        id = 3L,
        workout = Workout(
            title = "Upper Body Workout",
            exerciseWorkouts = listOf(mockExerciseWorkout1)
        ),
        completedDate = LocalDate.now().minusDays(3)
    )

}