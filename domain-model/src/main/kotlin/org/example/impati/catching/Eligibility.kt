package org.example.impati.catching

data class Eligibility(
    val value: String,
    val duplicable: Boolean,
) {

    companion object {

        fun basic(): Eligibility {
            return Eligibility("", true);
        }
    }
}
