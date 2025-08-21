package com.zahid.mathly.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahid.mathly.R
import kotlin.math.*
import kotlin.math.PI

@Composable
fun EmptyStateView(
    title: String,
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    // Animation for the arrow
    val infiniteTransition = rememberInfiniteTransition(label = "arrowAnimation")
    
    // Animate the arrow's progress along the path
    val arrowProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "arrowProgress"
    )
    
    // Animate the arrow's opacity
    val arrowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrowAlpha"
    )

    val pColor = MaterialTheme.colorScheme.primary.copy(alpha = arrowAlpha)
    
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp)
        ) {
            // Icon in a circular background
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Title
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Message
            Text(
                text = message,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "click '+' to add new",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            // Animated circular arrow pointing to the FAB
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val width = size.width
                    val height = size.height
                    
                    // Calculate center point (start of arrow)
                    val centerX = width / 2f
                    val startY = 0f
                    
                    // Calculate end point (FAB position, bottom right)
                    val endX = width - 60f
                    val endY = height - 20f
                    
                    // Create a circular path from center to FAB
                    val path = Path().apply {
                        // Start from center bottom
                        moveTo(centerX, startY)
                        
                        // Create a circular arc path to the FAB
                        cubicTo(
                            centerX + width * 0.4f, startY + height * 0.3f,  // First control point
                            endX - width * 0.3f, endY - height * 0.3f,       // Second control point
                            endX, endY                                       // End point
                        )
                    }
                    
                    // Get the point along the path based on animation progress
                    val pathMeasure = PathMeasure()
                    pathMeasure.setPath(path, false)
                    val pathLength = pathMeasure.length
                    val distance = pathLength * arrowProgress
                    
                    // Calculate current position manually based on the Bezier curve
                    val t = arrowProgress  // Normalized time parameter (0 to 1)
                    val mt = 1 - t  // Complement of t
                    
                    // Bezier curve position calculation
                    // For cubic Bezier: P = (1-t)³P₀ + 3(1-t)²tP₁ + 3(1-t)t²P₂ + t³P₃
                    val p0x = centerX
                    val p0y = startY
                    val p1x = centerX + width * 0.4f
                    val p1y = startY + height * 0.3f
                    val p2x = endX - width * 0.3f
                    val p2y = endY - height * 0.3f
                    val p3x = endX
                    val p3y = endY
                    
                    // Calculate current position
                    val currentX = mt*mt*mt*p0x + 3*mt*mt*t*p1x + 3*mt*t*t*p2x + t*t*t*p3x
                    val currentY = mt*mt*mt*p0y + 3*mt*mt*t*p1y + 3*mt*t*t*p2y + t*t*t*p3y
                    
                    // Calculate tangent (derivative of the Bezier curve)
                    // B'(t) = 3(1-t)²(P₁-P₀) + 6(1-t)t(P₂-P₁) + 3t²(P₃-P₂)
                    val tangentX = 3*mt*mt*(p1x-p0x) + 6*mt*t*(p2x-p1x) + 3*t*t*(p3x-p2x)
                    val tangentY = 3*mt*mt*(p1y-p0y) + 6*mt*t*(p2y-p1y) + 3*t*t*(p3y-p2y)
                    
                    // Normalize the tangent
                    val tangentLength = sqrt(tangentX*tangentX + tangentY*tangentY)
                    val normalizedTanX = if (tangentLength > 0) tangentX/tangentLength else 0f
                    val normalizedTanY = if (tangentLength > 0) tangentY/tangentLength else 0f
                    
                    // Draw the path (only the part that's been "traveled")
                    val progressPath = Path()
                    if (pathLength > 0) {
                        // We can't use getSegment since it requires getPosTan
                        // Instead, create a new path up to the current t value
                        progressPath.moveTo(p0x, p0y)
                        
                        // Number of segments to approximate the curve
                        val segments = 30
                        for (i in 1..segments) {
                            val segT = i.toFloat() / segments
                            if (segT > t) break
                            
                            val segMt = 1 - segT
                            val segX = segMt*segMt*segMt*p0x + 3*segMt*segMt*segT*p1x + 3*segMt*segT*segT*p2x + segT*segT*segT*p3x
                            val segY = segMt*segMt*segMt*p0y + 3*segMt*segMt*segT*p1y + 3*segMt*segT*segT*p2y + segT*segT*segT*p3y
                            
                            progressPath.lineTo(segX, segY)
                        }
                        
                        // Add the final point
                        progressPath.lineTo(currentX, currentY)
                    }
                    
                    // Draw the path
                    drawPath(
                        path = progressPath,
                        color = pColor,
                        style = Stroke(
                            width = 3f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                        )
                    )
                    
                    // Only draw arrowhead when we're at least halfway through the animation
                    if (arrowProgress > 0.5f) {
                        // Calculate arrowhead points
                        val arrowHeadLength = 15f
                        val angle = atan2(normalizedTanY, normalizedTanX)
                        val arrowPoint1X = currentX - arrowHeadLength * cos(angle - 0.5f)
                        val arrowPoint1Y = currentY - arrowHeadLength * sin(angle - 0.5f)
                        val arrowPoint2X = currentX - arrowHeadLength * cos(angle + 0.5f)
                        val arrowPoint2Y = currentY - arrowHeadLength * sin(angle + 0.5f)
                        
                        // Draw arrowhead
                        drawLine(
                            color = pColor,
                            start = Offset(currentX, currentY),
                            end = Offset(arrowPoint1X, arrowPoint1Y),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )

                        drawLine(
                            color = pColor,
                            start = Offset(currentX, currentY),
                            end = Offset(arrowPoint2X, arrowPoint2Y),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )
                    }

                    // Draw an arrow at the end position
                    if (arrowProgress > 0.9f) {
                        // Draw arrow head at the end position
                        val arrowHeadSize = 20f
                        
                        // Calculate the angle at the end of the path
                        // Use the tangent at t=1.0 (end of curve)
                        val endTangentX = 3*(p3x-p2x)  // Tangent at t=1 simplifies to 3(P₃-P₂)
                        val endTangentY = 3*(p3y-p2y)
                        val endAngle = atan2(endTangentY, endTangentX)
                        
                        // Draw the main arrow head triangle
                        val arrowPath = Path().apply {
                            // Start at the tip of the arrow
                            moveTo(endX, endY)
                            
                            // Calculate the two points of the arrow base
                            // Rotate the points around the tip by the angle of the path
                            val baseX1 = endX - arrowHeadSize * cos(endAngle - PI.toFloat()/6)
                            val baseY1 = endY - arrowHeadSize * sin(endAngle - PI.toFloat()/6)
                            val baseX2 = endX - arrowHeadSize * cos(endAngle + PI.toFloat()/6)
                            val baseY2 = endY - arrowHeadSize * sin(endAngle + PI.toFloat()/6)
                            
                            lineTo(baseX1, baseY1)
                            lineTo(baseX2, baseY2)
                            close()  // Connect back to the tip
                        }
                        
                        drawPath(
                            path = arrowPath,
                            color = pColor,
                            style = Fill
                        )
                    }
                }
            }
        }
    }
}
