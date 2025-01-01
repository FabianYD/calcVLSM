let contadorLAN = 1;

document.getElementById('agregarHost').addEventListener('click', function() {
    contadorLAN++;
    
    const div = document.createElement('div');
    div.className = 'host-entry';
    div.innerHTML = '<div class="row">' +
        '<div class="col-md-5">' +
            '<input type="text" class="form-control" name="nombreHost[]" ' +
            'value="LAN ' + contadorLAN + '" required>' +
        '</div>' +
        '<div class="col-md-5">' +
            '<input type="number" class="form-control" name="numHosts[]" ' +
            'min="1" required placeholder="Ej: 50">' +
        '</div>' +
        '<div class="col-md-2 text-end">' +
            '<button type="button" class="btn btn-danger remove-host">' +
                '<i class="bi bi-x-lg"></i>' +
            '</button>' +
        '</div>' +
    '</div>';
    
    document.getElementById('hostsContainer').appendChild(div);
});

// Manejar eliminación de hosts
document.addEventListener('click', function(e) {
    if (e.target.closest('.remove-host')) {
        e.target.closest('.host-entry').remove();
        
        // Actualizar nombres de LAN
        const hostEntries = document.querySelectorAll('.host-entry');
        hostEntries.forEach((entry, index) => {
            const input = entry.querySelector('input[name="nombreHost[]"]');
            input.value = 'LAN ' + (index + 1);
        });
        contadorLAN = hostEntries.length;
    }
});
