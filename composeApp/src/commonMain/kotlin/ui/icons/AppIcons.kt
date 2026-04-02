package ui.icons

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object AppIcons {
    val ShoppingCartIcon: ImageVector
        get() = ImageVector.Builder(
            name = "ShoppingCartIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // círculo izquierdo
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4f, 19f)
                arcToRelative(2f, 2f, 0f, true, true, 4f, 0f)
                arcToRelative(2f, 2f, 0f, true, true, -4f, 0f)
            }

            // círculo derecho
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(15f, 19f)
                arcToRelative(2f, 2f, 0f, true, true, 4f, 0f)
                arcToRelative(2f, 2f, 0f, true, true, -4f, 0f)
            }

            // carrito y manija
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(17f, 17f)
                horizontalLineToRelative(-11f)
                verticalLineToRelative(-14f)
                horizontalLineToRelative(-2f)
            }

            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(6f, 5f)
                lineToRelative(14f, 1f)
                lineToRelative(-1f, 7f)
                horizontalLineToRelative(-13f)
            }
        }.build()

    val ListIcon: ImageVector
        get() = ImageVector.Builder(
            name = "ListIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Líneas horizontales
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = androidx.compose.ui.graphics.StrokeCap.Round,
                strokeLineJoin = androidx.compose.ui.graphics.StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9f, 6f)
                lineTo(20f, 6f)

                moveTo(9f, 12f)
                lineTo(20f, 12f)

                moveTo(9f, 18f)
                lineTo(20f, 18f)
            }

            // Puntos de la lista
            path(
                fill = SolidColor(Color.Black),
                stroke = null
            ) {
                moveTo(5f, 6f)
                lineTo(5f, 6.01f)

                moveTo(5f, 12f)
                lineTo(5f, 12.01f)

                moveTo(5f, 18f)
                lineTo(5f, 18.01f)
            }
        }.build()

    val ChartBarIcon: ImageVector
        get() = ImageVector.Builder(
            name = "ChartBarIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // barra 1
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3f, 13f)
                horizontalLineToRelative(4f)
                verticalLineToRelative(6f)
                horizontalLineToRelative(-4f)
                close()
            }

            // barra 2
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(15f, 9f)
                horizontalLineToRelative(4f)
                verticalLineToRelative(10f)
                horizontalLineToRelative(-4f)
                close()
            }

            // barra 3
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9f, 5f)
                horizontalLineToRelative(4f)
                verticalLineToRelative(14f)
                horizontalLineToRelative(-4f)
                close()
            }

            // línea base
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4f, 20f)
                horizontalLineToRelative(14f)
            }
        }.build()

    val CircleCheckIcon: ImageVector
        get() = ImageVector.Builder(
            name = "CircleCheckIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // círculo
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3f, 12f)
                arcToRelative(9f, 9f, 0f, true, true, 18f, 0f)
                arcToRelative(9f, 9f, 0f, true, true, -18f, 0f)
            }

            // check
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9f, 12f)
                lineToRelative(2f, 2f)
                lineToRelative(4f, -4f)
            }
        }.build()

    val InfoCircleIcon: ImageVector
        get() = ImageVector.Builder(
            name = "InfoCircleIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // círculo
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3f, 12f)
                arcToRelative(9f, 9f, 0f, true, true, 18f, 0f)
                arcToRelative(9f, 9f, 0f, true, true, -18f, 0f)
            }

            // punto de info
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 9f)
                lineToRelative(0.01f, 0f)
            }

            // barra de info
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11f, 12f)
                verticalLineToRelative(4f)
                horizontalLineToRelative(1f)
            }
        }.build()

    val PlusIcon: ImageVector
        get() = ImageVector.Builder(
            name = "PlusIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Línea vertical
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = androidx.compose.ui.graphics.StrokeCap.Round,
                strokeLineJoin = androidx.compose.ui.graphics.StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 5f)
                lineTo(12f, 19f)
            }
            // Línea horizontal
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = androidx.compose.ui.graphics.StrokeCap.Round,
                strokeLineJoin = androidx.compose.ui.graphics.StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5f, 12f)
                lineTo(19f, 12f)
            }
        }.build()

    val TrashIcon: ImageVector
        get() = ImageVector.Builder(
            name = "TrashIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Línea superior de la papelera
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4f, 7f)
                lineTo(20f, 7f)
            }
            // Palos verticales internos
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(10f, 11f)
                lineTo(10f, 17f)
                moveTo(14f, 11f)
                lineTo(14f, 17f)
            }
            // Cuerpo de la papelera
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5f, 7f)
                lineTo(6f, 19f)
                arcToRelative(2f, 2f, 0f, false, false, 2f, 2f)
                horizontalLineToRelative(8f)
                arcToRelative(2f, 2f, 0f, false, false, 2f, -2f)
                lineTo(19f, 7f)
            }
            // Tapa de la papelera
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9f, 7f)
                verticalLineToRelative(-3f)
                arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                horizontalLineToRelative(4f)
                arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                verticalLineToRelative(3f)
            }
        }.build()

    val PencilIcon: ImageVector
        get() = ImageVector.Builder(
            name = "PencilIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Trazo principal del lápiz
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4f, 20f)
                horizontalLineToRelative(4f)
                lineToRelative(10.5f, -10.5f)
                arcToRelative(2.828f, 2.828f, 0f, true, false, -4f, -4f)
                lineToRelative(-10.5f, 10.5f)
                verticalLineToRelative(4f)
            }
            // Detalle del lápiz (trazo de punta)
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(13.5f, 6.5f)
                lineToRelative(4f, 4f)
            }
        }.build()

    val FloppyIcon: ImageVector
        get() = ImageVector.Builder(
            name = "FloppyIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 4f)
                horizontalLineTo(16f)
                lineToRelative(4f, 4f)
                verticalLineTo(18f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                horizontalLineTo(4f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                verticalLineTo(6f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(10f, 14f)
                arcToRelative(2f, 2f, 0f, true, true, 4f, 0f)
                arcToRelative(2f, 2f, 0f, true, true, -4f, 0f)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(14f, 4f)
                verticalLineTo(8f)
                horizontalLineTo(8f)
                verticalLineTo(4f)
            }
        }.build()

    val XIcon: ImageVector
        get() = ImageVector.Builder(
            name = "XIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18f, 6f)
                lineTo(6f, 18f)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 6f)
                lineTo(18f, 18f)
            }
        }.build()

    val CheckIcon = ImageVector.Builder(
        name = "CheckIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(5f, 12f)
            lineTo(10f, 17f)
            lineTo(20f, 7f)
        }
    }.build()

    val NotesIcon = ImageVector.Builder(
        name = "NotesIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        // 📄 Contenedor (libreta)
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(5f, 5f)
            arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
            horizontalLineToRelative(10f)
            arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
            verticalLineToRelative(14f)
            arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
            horizontalLineToRelative(-10f)
            arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
            close()
        }

        // 📝 Líneas de texto
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(9f, 7f)
            horizontalLineTo(15f)

            moveTo(9f, 11f)
            horizontalLineTo(15f)

            moveTo(9f, 15f)
            horizontalLineTo(13f)
        }
    }.build()
}