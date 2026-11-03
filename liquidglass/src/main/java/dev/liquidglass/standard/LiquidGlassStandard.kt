package dev.liquidglass.standard

/** Product standards; historical package names do not determine the selected renderer. */
enum class LiquidGlassStandard(val label: String) {
    Ios26("iOS 26"),
    Ios27("iOS 27");

    /** iOS 27's RuntimeShader pipeline is only entered on Android 13 and newer. */
    fun supportedOn(sdkInt: Int): LiquidGlassStandard =
        if (this == Ios27 && sdkInt < 33) Ios26 else this
}
