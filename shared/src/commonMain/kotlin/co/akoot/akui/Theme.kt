package co.akoot.akui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Theme(
    val background: Color =          Color.White,
    val backgroundSecondary: Color = Color(0xFFA8A8A8),
    val backgroundTertiary: Color =  Color(0xFFEAEAEA),
    val primary: Color =             Color(0xFF000000),
    val secondary: Color =           Color(0xFF0073FF),
    val tertiary: Color =            Color(0xFF00FFA6),
    val error: Color =               Color(0xFFFF003B),
    val errorSecondary: Color =      Color(0xFFFF0000),
    val errorTertiary: Color =       Color(0xFFFF4800),
    val warning: Color =             Color(0xFFFFB700),
    val warningSecondary: Color =    Color(0xFFFF7700),
    val warningTertiary: Color =     Color(0xFFFFDB5B),
    val quote: Color =             Color(0xFFA8A8A8),
    val quoteSecondary: Color =    Color(0xFFA8A8A8),
    val quoteTertiary: Color =     Color(0xFFA8A8A8),
    val success: Color =             Color(0xFF49D500),
    val successSecondary: Color =    Color(0xFF49D500),
    val successTertiary: Color =     Color(0xFF49D500),
    val text: Color =                primary,
    val textSecondary: Color =       secondary,
    val textTertiary: Color =        tertiary,
    val button: Color =              primary,
    val buttonSecondary: Color =     secondary,
    val buttonTertiary: Color =      tertiary,
    val buttonError: Color =         error,
    val buttonWarning: Color =       warning,
    val buttonQuote: Color =       quote,
    val buttonSuccess: Color =       success,
    val buttonText: Color =          Color(0xFFFFFFFF),
    val buttonSecondaryText: Color = Color(0xFFFFFFFF),
    val buttonTertiaryText: Color =  Color(0xFF000000),
    val buttonErrorText: Color =     Color(0xFFFFFFFF),
    val buttonQuoteText: Color =     Color(0xFFFFFFFF),
    val buttonSuccessText: Color =     Color(0xFFFFFFFF),
    val buttonWarningText: Color =   primary,
    val buttonPaddingVertical: Dp = 12.dp,
    val buttonPaddingHorizontal: Dp = 24.dp,
    val buttonMaxElevation: Double = 12.0,
    val buttonCornerRadius: Dp = 24.dp,
) {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun CustomButton(
        onClick: () -> Unit,
        corner: Dp = buttonCornerRadius,
        modifier: Modifier = Modifier,
        containerColor: Color = button,
        contentColor: Color = buttonText,
        disabledContainerColor: Color = button.copy(0.5f),
        disabledContentColor: Color = buttonText.copy(0.5f),
        maxElevation: Double = buttonMaxElevation,
        border: BorderStroke? = null,
        contentPadding: PaddingValues = PaddingValues(buttonPaddingHorizontal, buttonPaddingVertical),
        interactionSource: MutableInteractionSource? = null,
        content:  @Composable (RowScope.() -> Unit)
    ) = androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(corner),
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        contentPadding = contentPadding,
        border = border,
        elevation = ButtonDefaults.buttonElevation((maxElevation / 2).dp, 0.dp, maxElevation.dp, maxElevation.dp, 0.dp),
        content = content,
        interactionSource = interactionSource
    )

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun CustomIconButton(
        onClick: () -> Unit,
        corner: Dp = buttonCornerRadius,
        modifier: Modifier = Modifier,
        containerColor: Color = button,
        contentColor: Color = buttonText,
        disabledContainerColor: Color = button.copy(0.5f),
        disabledContentColor: Color = buttonText.copy(0.5f),
        contentPadding: PaddingValues = PaddingValues(buttonPaddingHorizontal, buttonPaddingVertical),
        interactionSource: MutableInteractionSource? = null,
        content:  @Composable () -> Unit
    ) = androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = modifier.padding(contentPadding),
        shape = RoundedCornerShape(corner),
        colors = IconButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        interactionSource = interactionSource,
        content = content
    )

    @Composable
    fun Button(
        context: Context = Context.PRIMARY,
        onClick: () -> Unit,
        paddingVertical: Dp = buttonPaddingVertical,
        paddingHorizontal: Dp = buttonPaddingHorizontal,
        interactionSource: MutableInteractionSource? = null,
        content: @Composable (RowScope.() -> Unit)
    ) = CustomButton(
        onClick = onClick,
        containerColor = containerColor(context),
        contentColor = contentColor(context),
        content = content,
        contentPadding = PaddingValues(paddingHorizontal, paddingVertical),
        interactionSource = interactionSource
    )

    fun containerColor(context: Context): Color = when(context) {
        Context.PRIMARY -> button
        Context.SECONDARY -> buttonSecondary
        Context.TERTIARY -> buttonTertiary
        Context.ERROR -> buttonError
        Context.WARNING -> buttonWarning
        Context.SUCCESS -> buttonSuccess
        Context.QUOTE -> buttonQuote
    }

    fun contentColor(context: Context): Color = when(context) {
        Context.PRIMARY -> buttonText
        Context.SECONDARY -> buttonSecondaryText
        Context.TERTIARY -> buttonTertiaryText
        Context.ERROR -> buttonErrorText
        Context.WARNING -> buttonWarningText
        Context.SUCCESS -> buttonSuccessText
        Context.QUOTE -> buttonQuoteText
    }

    @Composable
    fun IconButton(
        context: Context = Context.PRIMARY,
        onClick: () -> Unit,
        paddingVertical: Dp = buttonPaddingVertical,
        paddingHorizontal: Dp = buttonPaddingHorizontal,
        interactionSource: MutableInteractionSource? = null,
        content: @Composable () -> Unit
    ) = CustomIconButton(
        onClick = onClick,
        containerColor = containerColor(context),
        contentColor = contentColor(context),
        content = content,
        contentPadding = PaddingValues(paddingHorizontal, paddingVertical),
        interactionSource = interactionSource
    )
}