package dev.liquidglass.sample

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.ios27.ios27PressedClickable
import dev.liquidglass.ios27.ios27RowTopSeparator
import dev.liquidglass.ios27.Ios27PressMaskEdges

/** 12740:24239: full-width native sections, measured in the original 402pt design space. */
@Composable
internal fun SectionedListExample(file: String, modifier: Modifier, onSelect: (String) -> Unit) {
    val colors = LocalIos27PageColors.current
    val groups = rememberSceneListGroups(file)
    val title = rememberSceneToolbar(file)?.largeTitle
    Column(modifier) {
        // Host already positions this component at safeTop + 54pt.
        // Source title occupies 61pt, then Content Area contributes 16pt.
        Box(Modifier.fillMaxWidth().height(61.dp).padding(horizontal = Ios27Tokens.ContentMargin.dp)) {
            title?.let { SceneGlyphText(it, colors.label, Modifier.semantics { heading(); contentDescription = "Title" }) }
        }
        Spacer(Modifier.height(16.dp))
        groups.forEach { group ->
            Box(Modifier.fillMaxWidth().height(39.dp).padding(start = Ios27Tokens.ContentMargin.dp, top = 8.dp)
                .semantics { heading(); contentDescription = group.heading.label }) {
                SceneGlyphText(group.heading.text, colors.secondaryLabel)
            }
            group.rows.forEachIndexed { index, item ->
                Box(
                    Modifier.fillMaxWidth().height(Ios27Tokens.RowHeight.dp)
                        // Every source row owns a separator, including the first
                        // row immediately following a section header.
                        .ios27RowTopSeparator()
                        .ios27PressedClickable(
                            shape = RoundedCornerShape(0.dp),
                            role = Role.Button,
                            maskEdges = Ios27PressMaskEdges(
                                top = Ios27Tokens.SeparatorRenderHeight.dp / 2,
                                bottom = if (index < group.rows.lastIndex) Ios27Tokens.SeparatorRenderHeight.dp / 2 else 0.dp,
                            ),
                            onClick = { onSelect(item.label) },
                        )
                        .semantics { contentDescription = item.label }
                        .padding(start = Ios27Tokens.ContentMargin.dp, top = 1.dp),
                ) {
                    SceneGlyphText(item.text, colors.label)
                }
            }
        }
    }
}
