<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Calculadora VLSM</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/styles.css">
    </head>
    <body>
        <div class="container mt-4">
            <h1 class="text-center">
                <i class="bi bi-calculator me-2"></i>
                Calculadora VLSM
            </h1>
            
            <form action="SvRed" method="POST">
                <!-- Configuración de Red -->
                <div class="card mb-4">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="bi bi-gear-fill me-2"></i>
                            Configuración de Red
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <label for="direccionBase" class="form-label">
                                    <i class="bi bi-hdd-network me-1"></i>
                                    Dirección IP Base
                                </label>
                                <input type="text" class="form-control form-control-lg" 
                                       id="direccionBase" name="direccionBase" 
                                       placeholder="Ej: 192.168.1.0" required 
                                       pattern="^(\d{1,3}\.){3}\d{1,3}$"
                                       title="Ingrese una dirección IP válida (ej: 192.168.1.0)">
                            </div>
                            <div class="col-md-6">
                                <label for="mascaraBase" class="form-label">
                                    <i class="bi bi-mask me-1"></i>
                                    Máscara Base
                                </label>
                                <input type="number" class="form-control form-control-lg" 
                                       id="mascaraBase" name="mascaraBase" 
                                       min="0" max="32" required
                                       placeholder="Ej: 24">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Configuración de Hosts -->
                <div class="card mb-4">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="bi bi-diagram-3-fill me-2"></i>
                            Configuración de Hosts
                        </h5>
                    </div>
                    <div class="card-body">
                        <div id="hostsContainer">
                            <div class="host-entry">
                                <div class="row align-items-center">
                                    <div class="col-md-5">
                                        <label class="form-label">Nombre de Subred</label>
                                        <input type="text" class="form-control" name="nombreHost[]" 
                                               value="LAN 1" required>
                                    </div>
                                    <div class="col-md-5">
                                        <label class="form-label">Número de Hosts</label>
                                        <input type="number" class="form-control" name="numHosts[]" 
                                               min="1" required placeholder="Ej: 50">
                                    </div>
                                    <div class="col-md-2 text-end">
                                        <label class="form-label d-block">&nbsp;</label>
                                        <button type="button" class="btn btn-danger remove-host">
                                            <i class="bi bi-x-lg"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <button type="button" class="btn-add-subnet" id="agregarHost">
                            <i class="bi bi-plus-circle"></i>
                            Agregar Host
                        </button>
                    </div>
                </div>

                <!-- Botón Calcular -->
                <div class="d-grid gap-2 mb-4">
                    <button type="submit" class="btn btn-primary btn-lg">
                        <i class="bi bi-calculator-fill me-2"></i>
                        Calcular
                    </button>
                </div>
            </form>

            <!-- Resultados -->
            <% if(request.getSession().getAttribute("red") != null) { %>
            <div class="row">
                <!-- Resultados Principales -->
                <div class="col-md-8">
                    <!-- Red Principal -->
                    <div class="resultado-item red">
                        <h5>
                            <i class="bi bi-diagram-2 me-2"></i>
                            Red Principal
                        </h5>
                        <div class="resultado-contenido">
                            <h1><%= request.getSession().getAttribute("red") %></h1>
                        </div>
                    </div>

                    <!-- Tabla de Subredes -->
                    <div class="resultado-item subredes">
                        <h5>
                            <i class="bi bi-table me-2"></i>
                            Tabla de Subredes
                        </h5>
                        <div class="resultado-contenido">
                            <%= request.getSession().getAttribute("resultados") %>
                        </div>
                    </div>
                    
                    <!-- Procedimiento -->
                    <div class="resultado-item detalles">
                        <h5>
                            <i class="bi bi-list-check me-2"></i>
                            Procedimiento Detallado
                        </h5>
                        <div class="resultado-contenido">
                            <%= request.getSession().getAttribute("subredes") %>
                        </div>
                    </div>
                </div>

                <!-- Cálculo de Hosts -->
                <div class="col-md-4">
                    <div class="resultado-item hosts">
                        <h5>
                            <i class="bi bi-lightning-fill me-2"></i>
                            Cálculo de Hosts
                        </h5>
                        <div class="resultado-contenido">
                            <%= request.getSession().getAttribute("hosts") %>
                        </div>
                    </div>
                </div>
            </div>
            <% } %>
        </div>

        <!-- Footer -->
        <footer class="footer">
            <div class="container">
                <p class="creador">
                    <i class="bi bi-code-slash me-2"></i>
                    Creado por Fabián Díaz
                </p>
                <p class="universidad">
                    <i class="bi bi-building me-2"></i>
                    Universidad Técnica del Norte
                </p>
            </div>
        </footer>

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="js/main.js"></script>
    </body>
</html>