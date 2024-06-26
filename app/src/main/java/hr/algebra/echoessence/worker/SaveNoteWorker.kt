package hr.algebra.echoessence.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import hr.algebra.echoessence.ui.extra.NotificationUtil

class SaveNoteWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        NotificationUtil.showNotification(applicationContext, "Note Saved", "Your note has been saved successfully.")

        return Result.success()
    }
}
