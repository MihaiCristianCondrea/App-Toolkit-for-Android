I've checked the `develop` branch of [App-Toolkit-for-Android](https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android/tree/develop?utm_source=chatgpt.com).

You currently have two separate indicator implementations:

| Component              | Current behavior                                                     |
| ---------------------- | -------------------------------------------------------------------- |
| `DotsIndicator.kt`     | Animates each dot's size using `updateTransition` and a 300 ms tween |
| `PageIndicatorDots.kt` | Used by `OnboardingFooter` to show onboarding progress               |

Your `CustomCarousel` and `OnboardingScreen` already use `HorizontalPager`, so there is no reason to replace the pagers.

I would introduce one reusable `AospPageIndicator` in `:library:core:ui`, then have both features use it.

The important design decision is to preserve Android 10's visual behavior while fixing its state synchronization. Android 10's original `PageIndicator.java` kept a queue of pending animation positions. Under rapid input, that queue could become stale relative to the pager. A modern Compose implementation can instead read the pager's latest position directly, avoiding that class of desynchronization.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://android.googlesource.com\&sz=32)

Git at Google



## 1. The reusable Compose component

Create:

`library/core/ui/src/main/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/core/ui/views/carousel/AospPageIndicator.kt`

This implementation follows Android 10's overall visual language: compact dots, a moving rounded capsule, approximately 42% inactive opacity, and support for motion in either direction.

There is one deliberate difference from AOSP: this uses a mathematically generated capsule rather than copying Google's original animated vector paths pixel for pixel. It should give you the characteristic effect, but an exact visual reproduction would require porting those XML path animations.

```

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.carousel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sin

private const val AOSP_ANIMATION_DURATION = 250
private const val AOSP_INACTIVE_ALPHA = 0.42f
private const val CAPSULE_STRETCH = 0.7f

/**
 * An Android 10 Quick Settings-inspired page indicator.
 *
 * The animation follows the pager's actual scroll position.
 * It does not maintain a queue of page transitions.
 *
 * For large page counts, the visible dots form a sliding window.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AospPageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    dotSize: Dp = 6.4.dp,
    dotSpacing: Dp = 3.2.dp,
    maxVisibleDots: Int = 7,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = selectedColor.copy(
        alpha = selectedColor.alpha * AOSP_INACTIVE_ALPHA
    ),
    pageDescription: String? = null,
) {
    val pageCount = pagerState.pageCount
    if (pageCount <= 1) return

    PageIndicatorCanvas(
        pageCount = pageCount,
        selectedIndex = pagerState.currentPage,
        positionProvider = {
            pageIndicatorPosition(
                currentPage = pagerState.currentPage,
                offsetFraction = pagerState.currentPageOffsetFraction,
                pageCount = pageCount,
            )
        },
        modifier = modifier,
        dotSize = dotSize,
        dotSpacing = dotSpacing,
        maxVisibleDots = maxVisibleDots,
        selectedColor = selectedColor,
        unselectedColor = unselectedColor,
        pageDescription = pageDescription,
    )
}

/**
 * Alternative for components that own an integer selected index
 * rather than a PagerState.
 *
 * New targets interrupt previous animations. Large page jumps
 * snap directly to their destination.
 */
@Composable
fun AospPageIndicator(
    pageCount: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    dotSize: Dp = 6.4.dp,
    dotSpacing: Dp = 3.2.dp,
    maxVisibleDots: Int = 7,
    animationDurationMillis: Int = AOSP_ANIMATION_DURATION,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = selectedColor.copy(
        alpha = selectedColor.alpha * AOSP_INACTIVE_ALPHA
    ),
    pageDescription: String? = null,
) {
    if (pageCount <= 1) return

    require(animationDurationMillis >= 0)

    val selectedPage = selectedIndex.coerceIn(0, pageCount - 1)

    key(pageCount) {
        val animatedPosition = remember {
            Animatable(selectedPage.toFloat())
        }

        var previousTarget by remember {
            mutableIntStateOf(selectedPage)
        }

        LaunchedEffect(selectedPage, animationDurationMillis) {
            val isLargeJump = abs(selectedPage - previousTarget) > 1
            previousTarget = selectedPage

            if (isLargeJump || animationDurationMillis == 0) {
                animatedPosition.snapTo(selectedPage.toFloat())
            } else {
                animatedPosition.animateTo(
                    targetValue = selectedPage.toFloat(),
                    animationSpec = tween(
                        durationMillis = animationDurationMillis,
                        easing = FastOutSlowInEasing,
                    ),
                )
            }
        }

        PageIndicatorCanvas(
            pageCount = pageCount,
            selectedIndex = selectedPage,
            positionProvider = { animatedPosition.value },
            modifier = modifier,
            dotSize = dotSize,
            dotSpacing = dotSpacing,
            maxVisibleDots = maxVisibleDots,
            selectedColor = selectedColor,
            unselectedColor = unselectedColor,
            pageDescription = pageDescription,
        )
    }
}

@Composable
private fun PageIndicatorCanvas(
    pageCount: Int,
    selectedIndex: Int,
    positionProvider: () -> Float,
    modifier: Modifier,
    dotSize: Dp,
    dotSpacing: Dp,
    maxVisibleDots: Int,
    selectedColor: Color,
    unselectedColor: Color,
    pageDescription: String?,
) {
    require(dotSize > 0.dp)
    require(dotSpacing >= 0.dp)
    require(maxVisibleDots >= 2)

    val visibleDots = minOf(pageCount, maxVisibleDots)
    val dotStep = dotSize + dotSpacing
    val indicatorWidth = dotSize + dotStep * (visibleDots - 1)
    val indicatorHeight = maxOf(dotSize, 8.dp)

    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val description = pageDescription
        ?: "Page ${selectedIndex.coerceIn(0, pageCount - 1) + 1} of $pageCount"

    Canvas(
        modifier = modifier
            .semantics {
                stateDescription = description
            }
            .then(Modifier),
    ) {
        // Intentionally empty. The sized Canvas is below.
    }
}

```

The private Canvas function above needs its drawing body. Rather than keeping two Canvas elements, replace the entire `PageIndicatorCanvas` function with this completed version:

```

@Composable
private fun PageIndicatorCanvas(
    pageCount: Int,
    selectedIndex: Int,
    positionProvider: () -> Float,
    modifier: Modifier,
    dotSize: Dp,
    dotSpacing: Dp,
    maxVisibleDots: Int,
    selectedColor: Color,
    unselectedColor: Color,
    pageDescription: String?,
) {
    require(dotSize > 0.dp)
    require(dotSpacing >= 0.dp)
    require(maxVisibleDots >= 2)

    val visibleDots = minOf(pageCount, maxVisibleDots)
    val dotStep = dotSize + dotSpacing
    val indicatorWidth = dotSize + dotStep * (visibleDots - 1)
    val indicatorHeight = maxOf(dotSize, 8.dp)
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val description = pageDescription
        ?: "Page ${selectedIndex.coerceIn(0, pageCount - 1) + 1} of $pageCount"

    Canvas(
        modifier = modifier
            .semantics {
                stateDescription = description
            }
            .width(indicatorWidth)
            .height(indicatorHeight),
    ) {
        val diameter = dotSize.toPx()
        val radius = diameter / 2f
        val step = dotStep.toPx()
        val centerY = size.height / 2f

        // Read the animation position during drawing, rather
        // than recomposing the whole component on every frame.
        val position = positionProvider()
            .coerceIn(0f, (pageCount - 1).toFloat())

        val windowStart = pageIndicatorWindowStart(
            position = position,
            pageCount = pageCount,
            visibleDots = visibleDots,
        )

        fun centerX(page: Float): Float {
            val logicalX = radius + (page - windowStart) * step
            return if (isRtl) size.width - logicalX else logicalX
        }

        clipRect {
            val firstVisible = floor(windowStart)
                .toInt()
                .coerceAtLeast(0)

            val lastVisible = ceil(windowStart + visibleDots)
                .toInt()
                .coerceAtMost(pageCount - 1)

            // Inactive dots keep their fixed circular shape.
            for (index in firstVisible..lastVisible) {
                drawCircle(
                    color = unselectedColor,
                    radius = radius,
                    center = Offset(
                        x = centerX(index.toFloat()),
                        y = centerY,
                    ),
                )
            }

            // The fraction determines how far the active dot
            // has travelled towards the next page.
            val fraction = position - floor(position)

            // At the midpoint the circle becomes a capsule.
            // At integer page positions it becomes a circle again.
            val stretch = sin(PI * fraction).toFloat()
            val capsuleWidth =
                diameter + step * CAPSULE_STRETCH * stretch

            val activeCenterX = centerX(position)

            drawRoundRect(
                color = selectedColor,
                topLeft = Offset(
                    x = activeCenterX - capsuleWidth / 2f,
                    y = centerY - radius,
                ),
                size = Size(
                    width = capsuleWidth,
                    height = diameter,
                ),
                cornerRadius = CornerRadius(radius),
            )
        }
    }
}

/**
 * Converts PagerState's nearest-page representation into one
 * continuous position across all pages.
 */
internal fun pageIndicatorPosition(
    currentPage: Int,
    offsetFraction: Float,
    pageCount: Int,
): Float {
    if (pageCount <= 0) return 0f

    return (currentPage + offsetFraction)
        .coerceIn(0f, (pageCount - 1).toFloat())
}

/**
 * Keeps the active page near the middle of the visible window.
 * The window moves continuously for large carousels.
 */
internal fun pageIndicatorWindowStart(
    position: Float,
    pageCount: Int,
    visibleDots: Int,
): Float {
    if (pageCount <= visibleDots) return 0f

    val centerOffset = (visibleDots - 1) / 2f
    val maximumStart = (pageCount - visibleDots).toFloat()

    return (position - centerOffset)
        .coerceIn(0f, maximumStart)
}

```

Add these two imports to `AospPageIndicator.kt`:

```

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width

```

This gives you a component with two ways to use it.

- `AospPageIndicator(pagerState = pagerState)` follows the finger continuously.
- `AospPageIndicator(pageCount = 4, selectedIndex = index)` uses a cancellable 250 ms animation for non-pager components.

The `maxVisibleDots` logic is an App Toolkit extension rather than AOSP behavior. It lets you support long carousels without creating an indicator wider than the screen.

## 2. Replace the carousel indicator

File: [CustomCarousel.kt](https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android/blob/develop/library/core/ui/src/main/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/core/ui/views/carousel/CustomCarousel.kt?utm_source=chatgpt.com)

At the end of the existing `Column`, replace:

```

DotsIndicator(
    modifier = Modifier
        .align(alignment = Alignment.CenterHorizontally)
        .padding(bottom = SizeConstants.SmallSize),
    totalDots = currentItems.size,
    selectedIndex = pagerState.currentPage,
    dotSize = SizeConstants.MediumSize / 2,
)

```

With:

```

AospPageIndicator(
    pagerState = pagerState,
    modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(bottom = SizeConstants.SmallSize),
    dotSize = SizeConstants.MediumSize / 2,
)

```

That's the entire carousel integration. It already supplies the correct `PagerState`, and the reusable component calculates the position itself.

This is important because your previous code passed only `currentPage`. That meant the indicator knew which page was selected but not exactly where the finger was during the swipe.

The new implementation uses `currentPageOffsetFraction`, which gives the position relative to the nearest page.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://developer.android.com\&sz=32)

developer.android.com



## 3. Replace the onboarding indicator

File: [OnboardingFooter.kt](https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android/blob/develop/library/feature/onboarding/src/main/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/feature/onboarding/ui/views/controls/OnboardingFooter.kt?utm_source=chatgpt.com)

Add the import:

```

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.carousel.AospPageIndicator

```

In the center `Box`, replace `PageIndicatorDots` with:

```

Box(
    modifier = Modifier.weight(1f),
    contentAlignment = Alignment.Center,
) {
    AospPageIndicator(
        pagerState = pagerState,
        dotSize = SizeConstants.SmallSize,
    )
}

```

Your existing Back, Next, Done, and Skip behavior remains unchanged. The pager navigation in `OnboardingScreen.kt` also requires no modifications.

Notice that the indicator now tracks both manual swipes and `animateScrollToPage()` calls. The visual progress is tied to the actual pager movement, so button-driven navigation gets the same animation.

Once both call sites have been migrated and you've confirmed there are no other consumers, the following old implementations become redundant:

- `DotsIndicator.kt`
- `IndicatorDot.kt`
- `PageIndicatorDots.kt`

Because these are currently public composables, deleting them changes the library API. That fits a deliberate 3.0.0 breaking release, but any external consumers need migrating.

## 4. Test the geometry and rapid changes

Create the test file under:

`library/core/ui/src/test/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/core/ui/views/carousel/AospPageIndicatorGeometryTest.kt`

```

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.carousel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AospPageIndicatorGeometryTest {

    @Test
    fun `position follows a forward swipe`() {
        val position = pageIndicatorPosition(
            currentPage = 0,
            offsetFraction = 0.4f,
            pageCount = 3,
        )

        assertEquals(0.4f, position, 0.0001f)
    }

    @Test
    fun `position remains continuous when current page changes`() {
        val before = pageIndicatorPosition(
            currentPage = 0,
            offsetFraction = 0.49f,
            pageCount = 3,
        )

        val after = pageIndicatorPosition(
            currentPage = 1,
            offsetFraction = -0.49f,
            pageCount = 3,
        )

        assertEquals(0.49f, before, 0.0001f)
        assertEquals(0.51f, after, 0.0001f)
    }

    @Test
    fun `position follows a backward swipe`() {
        val position = pageIndicatorPosition(
            currentPage = 2,
            offsetFraction = -0.4f,
            pageCount = 4,
        )

        assertEquals(1.6f, position, 0.0001f)
    }

    @Test
    fun `position cannot exceed last page`() {
        val position = pageIndicatorPosition(
            currentPage = 4,
            offsetFraction = 0.5f,
            pageCount = 5,
        )

        assertEquals(4f, position)
    }

    @Test
    fun `empty pager has safe position`() {
        assertEquals(
            0f,
            pageIndicatorPosition(0, 0f, 0),
        )
    }

    @Test
    fun `short carousel needs no moving window`() {
        assertEquals(
            0f,
            pageIndicatorWindowStart(
                position = 1f,
                pageCount = 3,
                visibleDots = 7,
            ),
        )
    }

    @Test
    fun `large carousel centers active page`() {
        assertEquals(
            1f,
            pageIndicatorWindowStart(
                position = 4f,
                pageCount = 12,
                visibleDots = 7,
            ),
        )
    }

    @Test
    fun `window cannot extend beyond final page`() {
        assertEquals(
            5f,
            pageIndicatorWindowStart(
                position = 11f,
                pageCount = 12,
                visibleDots = 7,
            ),
        )
    }
}

```

These tests cover the mathematical behavior, not the actual animated frames. For the glitch you remember, I would also add a Compose UI integration test that rapidly changes the target page and verifies that the last selection wins.

Manually test these scenarios on a device:

1. Slowly drag halfway between two onboarding pages, then release backward.
2. Swipe forward twice rapidly, then immediately backward.
3. Repeatedly press Next and Back during an animation.
4. Change orientation while the carousel is on a middle page.
5. Test right-to-left layout direction.
6. Test a carousel containing more than seven pages.

The specific old Android 10 glitch hasn't been conclusively traced to one bug, but removing its pending-position queue avoids a plausible source of stale page selection.

## 5. Motion comparison

Page 1Drag to simulate swipe progressPage 3

ResetNext page

This interactive illustration represents the Compose Canvas geometry. It does not render the actual AOSP vector drawable animation.

## 6. A few design decisions I'd retain

There are some subtle differences between the original Android 10 behavior and what works better for App Toolkit:

| Detail                | Android 10 AOSP                 | Proposed App Toolkit                              |
| --------------------- | ------------------------------- | ------------------------------------------------- |
| Movement              | Discrete animated vector states | Continuous with finger movement                   |
| Shape                 | Animated vector path morphs     | Rounded capsule stretch                           |
| Inactive opacity      | 42%                             | 42%                                               |
| Transition timing     | 250 ms                          | Pager-controlled, or 250 ms for integer selection |
| Fast swipes           | Queued animation positions      | Latest pager position                             |
| Long page collections | Primarily small QS page sets    | Sliding window                                    |
| RTL                   | SystemUI-dependent              | Explicit layout direction support                 |

The AOSP dimensions also provide a useful reference: approximately 6.4 dp dot width, 9.6 dp center spacing, and 8 dp indicator height.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://android.googlesource.com\&sz=32)

Git at Google



One final accessibility refinement: the shared component accepts `pageDescription` so callers can pass localized strings instead of relying on the default English state description.

My preferred implementation for App Toolkit is the pager-driven version, because it retains the characteristic Android 10 capsule transformation but eliminates the need for a second animation state machine. It also stays independent of the onboarding feature.

The samples above are adapted to the repository's inspected `develop` source, but I haven't modified the repository or run a Gradle build. They should be compiled and visually checked before replacing the existing public composables.

If you eventually want a pixel-exact restoration, rather than this Compose interpretation, the next step is to recreate the original `major_*` and `minor_*` vector transitions from Android 10's SystemUI resources.