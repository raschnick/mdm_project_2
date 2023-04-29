function checkFiles(files) {

    console.log("checkFiles has been called...")

    if (files) {
        preview.src = URL.createObjectURL(files[0])
    }

    const formData = new FormData();
    for (const name in files) {
        formData.append("image", files[name]);
    }

    fetch('/analyze', {
        method: 'POST',
        headers: {},
        body: formData
    }).then(
        response => {
            console.log(response)
            console.log("foobar")
            response.text().then(function (text) {
                answer.innerHTML = text;
            });
        }
    ).then(
        success => console.log(success)
    ).catch(
        error => console.log(error)
    );


}


