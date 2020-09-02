package de.rki.coronawarnapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import de.rki.coronawarnapp.R
import de.rki.coronawarnapp.http.WebRequestBuilder
import de.rki.coronawarnapp.http.playbook.PlaybookImpl
import de.rki.coronawarnapp.notification.NotificationHelper
import de.rki.coronawarnapp.util.ForegroundPocTracker
import timber.log.Timber
import java.util.Date

/**
 * One time background noise worker
 *
 * @see BackgroundWorkScheduler
 */
class BackgroundNoiseOneTimeWorker(
    val context: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {

    companion object {
        private val TAG: String = BackgroundNoiseOneTimeWorker::class.java.simpleName
        private val notificationID = TAG.hashCode()
    }

    /**
     * Work execution
     *
     * @return Result
     */
    override suspend fun doWork(): Result {
        var result = Result.success()

        try {

            //BackgroundWorkHelper.moveCoroutineWorkerToForeground(context.getString(R.string.notification_headline), "", notificationID, this)
            PlaybookImpl(WebRequestBuilder.getInstance()).dummy()

        } catch (e: Exception) {
            // TODO: Should we even retry here?
            result = if (runAttemptCount > BackgroundConstants.WORKER_RETRY_COUNT_THRESHOLD) {
                Result.failure()
            } else {
                Result.retry()
            }
        }
        ForegroundPocTracker.save(context, TAG, Date(), result)
        return result
    }
}
