import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import kotlin.random.Random

class CloudDrawable : Drawable() {

    private val colors = listOf(
        Color.parseColor("#D2E3C8"), // Light Green
        Color.parseColor("#F8E7EC"), // Light Pink
        Color.parseColor("#E4F1FF"), // Light Blue
        Color.parseColor("#FEFDEF")  // Light Yellow
    )

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun draw(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()

        // Draw random cloud shapes
        for (i in 0..5) {
            paint.color = colors.random()
            val cloudPath = createRandomCloudPath(width, height)
            canvas.drawPath(cloudPath, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = android.graphics.PixelFormat.OPAQUE

    private fun createRandomCloudPath(width: Int, height: Int): Path {
        val path = Path()
        val random = Random.Default
        val x = random.nextInt(width)
        val y = random.nextInt(height)
        val size = random.nextInt(50, 150)

        path.moveTo(x.toFloat(), y.toFloat())
        path.cubicTo(x - size.toFloat(), y - size.toFloat(), x + size.toFloat(), y - size.toFloat(), x.toFloat(), y.toFloat())
        path.cubicTo(x + size.toFloat(), y - size.toFloat(), x + size.toFloat(), y + size.toFloat(), x.toFloat(), y.toFloat())
        path.cubicTo(x + size.toFloat(), y + size.toFloat(), x - size.toFloat(), y + size.toFloat(), x.toFloat(), y.toFloat())
        path.cubicTo(x - size.toFloat(), y + size.toFloat(), x - size.toFloat(), y - size.toFloat(), x.toFloat(), y.toFloat())

        return path
    }
}
