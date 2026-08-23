package co.akoot.akui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState

data class Theme(
    val background: Color =          Color(0xFFFFFFFF),
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
    val quote: Color =               Color(0xFFA8A8A8),
    val quoteSecondary: Color =      Color(0xFFA8A8A8),
    val quoteTertiary: Color =       Color(0xFFA8A8A8),
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
    val buttonQuote: Color =         quote,
    val buttonSuccess: Color =       success,
    val buttonText: Color =          background,
    val buttonSecondaryText: Color = background,
    val buttonTertiaryText: Color =  primary,
    val buttonErrorText: Color =     background,
    val buttonQuoteText: Color =     background,
    val buttonSuccessText: Color =   background,
    val buttonWarningText: Color =   primary,
    val buttonPaddingVertical: Dp = 12.dp,
    val buttonPaddingHorizontal: Dp = 24.dp,
    val buttonMaxElevation: Double = 4.0,
    val buttonCornerRadius: Dp = 24.dp,
    val textFieldCornerRadius: Dp = 32.dp,
) {
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

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun CustomButton(
        onClick: () -> Unit,
        corner: Dp = buttonCornerRadius,
        modifier: Modifier = Modifier,
        containerColor: Color = button,
        contentColor: Color = buttonText,
        containerColorHover: Color = button,
        contentColorHover: Color = buttonText,
        disabledContainerColor: Color = button.copy(0.5f),
        disabledContentColor: Color = buttonText.copy(0.5f),
        maxElevation: Double = buttonMaxElevation,
        border: BorderStroke? = null,
        contentPadding: PaddingValues = PaddingValues(buttonPaddingHorizontal, buttonPaddingVertical),
        content:  @Composable (RowScope.() -> Unit)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val hover by interactionSource.collectIsHoveredAsState()
        val containerColor by animateColorAsState(
            targetValue = if (hover) containerColorHover else containerColor,
        )
        val contentColor by animateColorAsState(
            targetValue = if (hover) contentColorHover else contentColor,
        )
        androidx.compose.material3.Button(
            onClick = onClick,
            modifier = modifier.pointerHoverIcon(PointerIcon.Hand),
            shape = RoundedCornerShape(corner),
            colors = ButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = disabledContainerColor,
                disabledContentColor = disabledContentColor
            ),
            contentPadding = contentPadding,
            border = border,
            elevation = ButtonDefaults.buttonElevation(
                (maxElevation / 2).dp,
                0.dp,
                maxElevation.dp,
                maxElevation.dp,
                0.dp
            ),
            content = content,
            interactionSource = interactionSource
        )
    }

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun CustomIconButton(
        onClick: () -> Unit,
        corner: Dp = buttonCornerRadius,
        modifier: Modifier = Modifier,
        containerColor: Color = button,
        contentColor: Color = buttonText,
        containerColorHover: Color = button,
        contentColorHover: Color = buttonText,
        disabledContainerColor: Color = button.copy(0.5f),
        disabledContentColor: Color = buttonText.copy(0.5f),
        rotate: Boolean = true,
        content:  @Composable () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val hover by interactionSource.collectIsHoveredAsState()
        val containerColor by animateColorAsState(
            targetValue = if (hover) containerColorHover else containerColor,
        )
        val contentColor by animateColorAsState(
            targetValue = if (hover) contentColorHover else contentColor,
        )
        val rotation by animateFloatAsState(
            targetValue = if (rotate && hover) 360f else 0f,
        )
        androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = modifier
            .pointerHoverIcon(PointerIcon.Hand)
            .rotate(rotation),
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
        }

    @Composable
    fun Button(
        context: Context = Context.PRIMARY,
        inverted: Boolean = true,
        paddingVertical: Dp = buttonPaddingVertical,
        paddingHorizontal: Dp = buttonPaddingHorizontal,
        elevation: Double = buttonMaxElevation,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable (RowScope.() -> Unit)
    ) = CustomButton(
        onClick = onClick,
        containerColor = if(inverted) button else containerColor(context),
        contentColor = if(inverted) buttonText else contentColor(context),
        containerColorHover = if(inverted) containerColor(context) else button,
        contentColorHover = if(inverted) contentColor(context) else buttonText,
        content = content,
        contentPadding = PaddingValues(paddingHorizontal, paddingVertical),
        maxElevation = elevation,
        modifier = modifier
    )

    @Composable
    fun IconButton(
        context: Context = Context.PRIMARY,
        inverted: Boolean = true,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        CustomIconButton(
            onClick = onClick,
            containerColor = if(inverted) button else containerColor(context),
            contentColor = if(inverted) buttonText else contentColor(context),
            containerColorHover = if(inverted) containerColor(context) else button,
            contentColorHover = if(inverted) contentColor(context) else buttonText,
            content = content,
            modifier = modifier
        )
    }

    @Composable
    fun CustomPasswordField(
        text: String = "",
        showPassword: Boolean = false,
        textColor: Color = primary,
        cursorColor: Color = textColor,
        backgroundColor: Color = primary.copy(0.1f),
        borderColor: Color = primary.copy(0.3f),
        roundedCorners: Dp = textFieldCornerRadius,
        borderStrokeWidth: Dp = 1.dp,
        paddingHorizontal: Dp = 8.dp,
        textAlign: TextAlign = TextAlign.Center,
        onType: (Key) -> Boolean = { true },
        icon: @Composable (() -> Unit)? = null,
        modifier: Modifier = Modifier,
        submit: (CharSequence) -> Unit
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(backgroundColor, RoundedCornerShape(roundedCorners))
                .border(borderStrokeWidth, borderColor, RoundedCornerShape(roundedCorners))
        ) {
            val state = rememberTextFieldState(text)
            icon?.let {
                Spacer(Modifier.padding(paddingHorizontal))
                it.invoke()
                Spacer(Modifier.padding(paddingHorizontal.div(2)))
            } ?: Spacer(Modifier.padding(paddingHorizontal))
            BasicSecureTextField(
                state = state,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                onKeyboardAction = { submit(state.text) },
                textObfuscationMode =
                    if (showPassword) {
                        TextObfuscationMode.Visible
                    } else {
                        TextObfuscationMode.RevealLastTyped
                    },
                textStyle = TextStyle(
                    color = textColor,
                    textAlign = textAlign
                ),
                cursorBrush = SolidColor(cursorColor),
                modifier = modifier
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown) {
                            onType(event.key)
                        } else {
                            true
                        }
                    }
            )
            Spacer(Modifier.padding(paddingHorizontal.div(2)))
            IconButton(
                Context.ERROR,
                onClick = { state.clearText() },
                modifier = Modifier.alpha(if(state.text.isNotEmpty()) 1f else 0f)
            ) {
                Icon(Icons.Default.Clear, "Clear")
            }
        }

    }

    @Composable
    fun CustomTextField(
        text: String = "",
        textColor: Color = primary,
        cursorColor: Color = primary,
        backgroundColor: Color = primary.copy(0.1f),
        selectionColor: Color = primary,
        borderColor: Color = primary.copy(0.3f),
        roundedCorners: Dp = textFieldCornerRadius,
        borderStrokeWidth: Dp = Dp.Hairline,
        paddingHorizontal: Dp = 8.dp,
        textAlign: TextAlign = TextAlign.Left,
        onType: (Key) -> Boolean = { true },
        lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
        labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Attached(),
        label: String? = null,
        placeholder: String? = null,
        prefix: String? = null,
        suffix: String? = null,
        supportingText: String? = null,
        icon: @Composable (() -> Unit)? = null,
        modifier: Modifier = Modifier,
        submit: (CharSequence) -> Boolean
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(backgroundColor, RoundedCornerShape(roundedCorners))
                .border(borderStrokeWidth, borderColor, RoundedCornerShape(roundedCorners))
        ) {
            val state = rememberTextFieldState(text)
            icon?.let {
                Spacer(Modifier.padding(paddingHorizontal))
                it.invoke()
                Spacer(Modifier.padding(paddingHorizontal.div(2)))
            } ?: Spacer(Modifier.padding(paddingHorizontal))
            TextField(
                labelPosition = labelPosition,
                label = label?.let { { Text(it, color = textColor) } },
                placeholder = placeholder?.let { { Text(it, color = textColor) } },
                prefix = prefix?.let { { Text(it, color = textColor) } },
                suffix = suffix?.let { { Text(it, color = textColor) } },
                supportingText = supportingText?.let { { Text(it, color = textColor.copy(0.5f)) } },
                enabled = true,
                state = state,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                onKeyboardAction = { if(submit(state.text)) state.clearText() },
                textStyle = TextStyle(
                    color = textColor,
                    textAlign = textAlign
                ),
                lineLimits = lineLimits,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedLabelColor = backgroundColor.copy(0.75f),
                    unfocusedLabelColor = backgroundColor.copy(0.5f),
                    cursorColor = cursorColor,
                    selectionColors = TextSelectionColors(
                        selectionColor,
                        selectionColor
                    ),
                ),
                contentPadding = PaddingValues.Zero,
                modifier = Modifier
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown) {
                            onType(event.key)
                        } else {
                            true
                        }
                    }
            )
            Spacer(Modifier.padding(paddingHorizontal.div(2)))
            IconButton(
                Context.ERROR,
                onClick = { state.clearText() },
                modifier = Modifier.alpha(if(state.text.isNotEmpty()) 1f else 0f)
            ) {
                Icon(Icons.Default.Clear, "Clear")
            }
        }
    }

    @Composable
    fun PasswordField(
        context: Context = Context.PRIMARY,
        showPassword: Boolean = false,
        textAlign: TextAlign = TextAlign.Center,
        onType: (Key) -> Boolean = { true },
        text: String = "",
        modifier: Modifier = Modifier,
        icon: @Composable (() -> Unit)? = null,
        submit: (CharSequence) -> Unit
    ) = CustomPasswordField(
        text = text,
        onType = onType,
        textAlign = textAlign,
        showPassword = showPassword,
        submit = submit,
        backgroundColor = background,
        textColor = primary,
        cursorColor = containerColor(context),
        borderColor = containerColor(context),
        modifier = modifier,
        icon = icon
    )

    @Composable
    fun TextField(
        context: Context = Context.PRIMARY,
        textAlign: TextAlign = TextAlign.Left,
        onType: (Key) -> Boolean = { true },
        text: String = "",
        label: String? = null,
        placeholder: String? = null,
        icon: @Composable (() -> Unit)? = null,
        prefix: String? = null,
        suffix: String? = null,
        supportingText: String? = null,
        modifier: Modifier = Modifier,
        submit: (CharSequence) -> Boolean
    ) = CustomTextField(
        text = text,
        onType = onType,
        textAlign = textAlign,
        submit = submit,
        backgroundColor = background,
        textColor = primary,
        cursorColor = containerColor(context),
        borderColor = containerColor(context),
        selectionColor = containerColor(context).copy(0.25f),
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        borderStrokeWidth = 0.dp,
        icon = icon,
    )

    @Composable
    fun Icon(
        icon: ImageVector,
        description: String = "",
        modifier: Modifier = Modifier,
        tint: Color = primary
    ) = androidx.compose.material3.Icon(icon, description, modifier, tint)

    @Composable
    fun TopBar(
        state: WindowState,
        title: String,
        arrangement: Arrangement.Horizontal = Arrangement.End,
        modifier: Modifier = Modifier,
        onClose: () -> Unit = {}
    ) {
        val onToggleMaximize = {
            state.placement =
                if (state.placement == WindowPlacement.Maximized) {
                    WindowPlacement.Floating
                } else {
                    WindowPlacement.Maximized
                }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                "[ $title ]",
                color = primary,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = arrangement
            ) {
                TopBarButton(
                    Icons.Default.Remove,
                    foreground = primary,
                    backgroundHover = primary,
                    foregroundHover = background,
                    onClick = { state.isMinimized = true }
                )
                TopBarButton(
                    if (state.placement == WindowPlacement.Floating) Icons.Default.Fullscreen else Icons.Default.FullscreenExit,
                    foreground = primary,
                    backgroundHover = primary,
                    foregroundHover = background,
                    onClick = onToggleMaximize
                )
                TopBarButton(
                    Icons.Default.Close,
                    foreground = primary,
                    backgroundHover = Color.Red,
                    onClick = onClose
                )
            }
        }
    }

    @Composable
    fun TopBarButton(
        icon: ImageVector,
        background: Color = Color.Transparent,
        foreground: Color = Color.White,
        backgroundHover: Color = background,
        foregroundHover: Color = foreground,
        description: String = "",
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val hover by interactionSource.collectIsHoveredAsState()
        val bg by animateColorAsState(
            targetValue = if (hover) backgroundHover else background,
        )
        val fg by animateColorAsState(
            targetValue = if (hover) foregroundHover else foreground,
        )
        val rotation by animateFloatAsState(
            targetValue = if (hover) 180f else 0f,
        )

        androidx.compose.material3.IconButton(
            onClick = onClick,
            interactionSource = interactionSource,
            modifier = Modifier
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = fg,
                modifier = Modifier
                    .background(bg, CircleShape)
                    .padding(4.dp)
                    .rotate(rotation)
            )
        }
    }
}

