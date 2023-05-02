const jsConfetti = new JSConfetti()


if (document.getElementById("text-result").innerHTML === "The URL is probably benign!") {
    jsConfetti.addConfetti({
        emojis: ['✅', '🦄', '😊'],
        emojiSize: 100,
        confettiNumber: 30,
    })
} else {
    jsConfetti.addConfetti({
        emojis: ['⚠️', '⛔', '☢️'],
        emojiSize: 100,
        confettiNumber: 30,
    })
}