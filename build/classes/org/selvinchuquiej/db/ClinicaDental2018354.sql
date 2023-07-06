/*
Nombre completo: Selvin Raúl Chuquiej Andrade
Código Técnico: IN5AM
Carnet: 2018354
Fecha de creación: 01/05/2022
Modificaciones: 10/05/2022
 */
Drop Database if exists DBClinicaDentalLFS2018354;
Create Database DBClinicaDentalLFS2018354;

Use DBClinicaDentalLFS2018354;

Create Table Paciente(
	codigoPaciente int not null,
    nombresPaciente varchar(50) not null,
    apellidosPaciente varchar(50) not null,
    sexo char not null,
    fechaNacimiento date not null,
    direccionPaciente varchar(100) not null,
    telefonoPersonal varchar(8),
    fechaPrimeraVisita date,
    primary key PK_codigoPaciente(codigoPaciente)
);

Create Table Especialidad(
	codigoEspecialidad int not null auto_increment,
    descripcion varchar(100) not null,
    primary key PK_codigoEspecialidad(codigoEspecialidad)
);

Create Table Medicamento(
	codigoMedicamento int not null auto_increment,
    nombreMedicamento varchar(100) not null,
    primary key PK_codigoMedicamento(codigoMedicamento)
);

Create Table Doctor(
	numeroColegiado int not null,
    nombresDoctor varchar(50) not null,
    apellidosDoctor varchar(50) not null,
	telefonoContacto varchar(8) not null,
    codigoEspecialidad int not null,
    primary key PK_numeroColegiado(numeroColegiado),
    constraint FK_Doctores_Especialidad foreign key (codigoEspecialidad)
		references Especialidad (codigoEspecialidad)
);

Create Table Receta(
	codigoReceta int not null auto_increment,
    fechaReceta date not null,
    numeroColegiado int not null,
    primary key PK_codigoReceta (codigoReceta),
    constraint FK_Recetas_Doctor foreign key (numeroColegiado)
		references Doctor(numeroColegiado)
);

Create Table DetalleReceta(
	codigoDetalleReceta int not null auto_increment,
	dosis varchar(100) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_odigoDetalleReceta (codigoDetalleReceta),
    constraint FK_DetalleReceta_Receta foreign key(codigoReceta)
		references Receta (codigoReceta),
	constraint FK_DetalleReceta_Medicamento foreign key (codigoMedicamento)
			references Medicamento (codigoMedicamento)
);

Create Table Cita(
	codigoCita int not null auto_increment,
    fechaCita date not null,
    horaCita time not null,
    tratamiento varchar(50) ,
    descripCondActual varchar(100) not null,
    codigoPaciente int not null,
    numeroColegiado int not null,
    primary key PK_codigoCita (codigoCita),
    constraint FK_Citas_Paciente foreign key (codigoPaciente)
		references Paciente (codigoPaciente),
	constraint FK_Citas_Doctor foreign key (numeroColegiado)
		references Doctor (numeroColegiado)
);

Create Table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario(codigoUsuario)
);

Create Table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster(usuarioMaster)
);

-- Pacientes
-- Agregar Paciente ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_AgregarPaciente(in codigoPaciente int, in nombresPaciente varchar(50), in apellidosPaciente varchar(50), 
										in sexo char, in fechaNacimiento date, in direccionPaciente varchar(100), in telefonoPersonal varchar(8), 
                                        in fechaPrimeraVisita date)
	Begin
		Insert Into Paciente(codigoPaciente, nombresPaciente, apellidosPaciente, sexo, 
								fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita)
			Values(codigoPaciente, nombresPaciente, apellidosPaciente, upper(sexo), 
					fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita);
    End $$
Delimiter ;

Call sp_AgregarPaciente(101 , 'Selvin Raúl', 'Chuquiej Andrade', 'm', '1987-05-26', 'Zona 18 Guatemala', '12563458', now());
Call sp_AgregarPaciente(102 , 'Sofia Fernanda', 'Sanchez Gomez', 'f', '1999-12-18', 'Zona 21 Guatemala', '56328965', now());
Call sp_AgregarPaciente(103 , 'Jose Manuel', 'Mendez Mende', 'm', '1999-12-18', 'Zona 6 Guatemala', '78545698', now());
Call sp_AgregarPaciente(104 , 'Bernardo Jose', 'Cetino Ruiz', 'm', '1999-12-18', 'Zona 8 Guatemala', '52365896', now());
Call sp_AgregarPaciente(105 , 'Gaspar	Marcelo', 'Pirír Sabán', 'm', '1999-12-18', 'Zona 7 Guatemala', '54521897', now());
Call sp_AgregarPaciente(106 , 'Gustavo	Martín', 'Muchuch Chutáz', 'm', '1999-12-18', 'Zona 3 Guatemala', '1256389', now());
Call sp_AgregarPaciente(107 , 'Renault Lego', 'Muchuch Chutá', 'm', '1999-12-18', 'Zona 21 Guatemala', '1723658', now());
Call sp_AgregarPaciente(108 , 'Nancy Beatriz', 'López López', 'f', '1999-12-18', 'Zona 5 Guatemala', '75321489', now());
Call sp_AgregarPaciente(109 , 'Antonio	Juárez', 'Cush Vásquez', 'm', '1999-12-18', 'Zona 17 Guatemala', '41256398', now());
Call sp_AgregarPaciente(110 , 'Azul	Julia', 'Carrera Vásqueer', 'f', '1999-12-18', 'Zona 16 Guatemala', '14523698', now());

-- Listar Paciente ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_ListarPacientes()
		Begin
			Select
				P.codigoPaciente, 
				P.nombresPaciente, 
				P.apellidosPaciente, 
				P.sexo, 
				P.fechaNacimiento, 
				P.direccionPaciente, 
				P.telefonoPersonal, 
				P.fechaPrimeraVisita
            From Paciente P;
        End $$
Delimiter ; 

Call sp_ListarPacientes();

-- Buscar Paciente ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_BuscarPaciente(in codPac int)
		Begin
			Select
				P.codigoPaciente, 
				P.nombresPaciente, 
				P.apellidosPaciente, 
				P.sexo, 
				P.fechaNacimiento, 
				P.direccionPaciente, 
				P.telefonoPersonal, 
				P.fechaPrimeraVisita
            From Paciente P
				where codigoPaciente = codPac;
    End $$
Delimiter ;

Call sp_BuscarPaciente(101);

-- Eliminar Paciente --------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EliminarPaciente(in codPac int)
		Begin
			Delete from Paciente
				where codigoPaciente = codPac;
        End $$
Delimiter ; 

-- Editar Paciente ----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EditarPaciente(in codPac int, in nombPaciente varchar(50), in apellPaciente varchar(50), 
										in sex char, in fechaNac date, in direccPac varchar(100), in telPersonal varchar(8), 
                                        in fechPrimVisita date)
		Begin
				Update Paciente P set
					P.nombresPaciente = nombPaciente,
					P.apellidosPaciente = apellPaciente,
					P.sexo = upper(sex),
					P.fechaNacimiento = fechaNac,
					P.direccionPaciente = direccPac,
					P.telefonoPersonal = telPersonal,
					P.fechaPrimeraVisita =  fechPrimVisita
					where codigoPaciente = codPac;
        End $$
Delimiter ; 


-- Especialidades 
-- Agregar Especialidad ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_AgregarEspecialidad (in descripcion varchar(100))
		Begin
			Insert Into Especialidad(descripcion)
				values (descripcion);
        End $$
Delimiter ; 
Call sp_AgregarEspecialidad('Cirugía oral y maxilofacial');
Call sp_AgregarEspecialidad('Endodoncia');
Call sp_AgregarEspecialidad('Patología maxilofacial y oral');
Call sp_AgregarEspecialidad('Ortodoncia');
Call sp_AgregarEspecialidad('Prostodoncia y odontología protésica');

-- Listar Especialidad ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_ListarEspecialidades()
		Begin
			Select
				E.codigoEspecialidad,
				E.descripcion
            From Especialidad E;
		End $$
Delimiter ;

Call sp_ListarEspecialidades();

-- Buscar Especialidad ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_BuscarEspecialidad(in codEsp int)
		Begin
			Select
				E.codigoEspecialidad,
                E.descripcion
			From Especialidad E
				where codigoEspecialidad = codEsp;
        End $$
Delimiter ;

Call sp_BuscarEspecialidad(1);

-- Eliminar Especialidad ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EliminarEspecialidad(in codEsp int)
		Begin
			Delete From Especialidad
			where codigoEspecialidad = codEsp;
        End $$
Delimiter ;

-- Editar Especialidad ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EditarEspecialidad(in codEsp int, in descr varchar(100))
		Begin
			Update Especialidad E set
				E.descripcion = descr
			where E.codigoEspecialidad = codEsp;
        End $$
Delimiter ; 

-- Medicamentos
-- Agregar Medicamento ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_AgregarMedicamento(in nombreMedicamento varchar(100))
		Begin
			Insert Into Medicamento(nombreMedicamento)
				values (nombreMedicamento);
        End $$
Delimiter ;

Call sp_AgregarMedicamento('Alka-selzer');
Call sp_AgregarMedicamento('IbuProfeno');
Call sp_AgregarMedicamento('Acetaminofen');
Call sp_AgregarMedicamento('Lanzoprazol');
Call sp_AgregarMedicamento('Raditidina');
Call sp_AgregarMedicamento('Esoprazol');
Call sp_AgregarMedicamento('Ex-Flu');
Call sp_AgregarMedicamento('CreceMax');
Call sp_AgregarMedicamento('Zulcrafato');
Call sp_AgregarMedicamento('Sertal');


-- Listar Medicamento ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_ListarMedicamentos()
		Begin
			Select
				M.codigoMedicamento,
                M.nombreMedicamento
			From Medicamento M;
        End $$
Delimiter ; 

Call sp_ListarMedicamentos();

-- Buscar Medicamento ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_BuscarMedicamento(in codMed int)
		Begin
			Select
				M.codigoMedicamento,
                M.nombreMedicamento
			From Medicamento M
				where codigoMedicamento = codMed;
        End $$
Delimiter ; 

Call sp_BuscarMedicamento (1);

-- Eliminar Medicamento ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EliminarMedicamento(in codMed int)
		Begin
			Delete From Medicamento
				where codigoMedicamento = codMed;
        End $$
Delimiter ; 

-- Editar Medicamento ---------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EditarMedicamento(in codMed int, in nomMed varchar(100))
		Begin
			Update Medicamento M set
				M.nombreMedicamento = nomMed
			where codigoMedicamento = codMed;
        End $$
Delimiter ;

-- Doctores
-- Agregar Doctores -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_AgregarDoctor(in numeroColegiado int, in nombresDoctor varchar(50), in apellidosDoctor varchar(50),
										in telefonoContacto varchar(8), in codigoEspecialidad int)
		Begin
			Insert Into Doctor(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad)
				values (numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad);
        End $$
Delimiter ;

Call sp_AgregarDoctor(12, 'Jose Manuel', 'Perez Albeño', '52894625', 1);
Call sp_AgregarDoctor(28, 'Abdul Amibael ', 'Pereira Péres', '45895625', 3);
Call sp_AgregarDoctor(36, 'Andres Noe', 'Gramajo Sagastume', '12569386', 2);
Call sp_AgregarDoctor(48, 'Catalino', 'Depaz Luis', '12578456', 3);
Call sp_AgregarDoctor(50, 'Dagoberto Alejandro', 'Rivera Pérez', '89563269', 5);
Call sp_AgregarDoctor(64, 'Cecilia Soifa', 'Samayoa Eguizabal', '56985682', 1);
Call sp_AgregarDoctor(74, 'Cesat Antonio', 'Leiva García', '45236987', 4);
Call sp_AgregarDoctor(84, 'Byron Rodolfo', 'Estrada Rodriguez', '23698741', 4);
Call sp_AgregarDoctor(95, 'Carmen Karla', 'Leiva García ', '58963147', 5);
Call sp_AgregarDoctor(101, 'Sandra Paola', 'Aliñado Asturias', '45612378', 2);

-- Listar Doctores -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_ListarDoctores()
		Begin
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoContacto,
                D.codigoEspecialidad
                From Doctor D;
        End $$
Delimiter ; 

Call sp_ListarDoctores();

-- Buscar Doctores -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_BuscarDoctor(in numCol int)
		Begin
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoContacto,
                D.codigoEspecialidad
			From Doctor D
				where numeroColegiado = numCol;
		End $$
Delimiter ; 

Call sp_BuscarDoctor(1);

-- Eliminar Doctores -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EliminarDoctor(in numCol int)
		Begin
			Delete From Doctor
				where numeroColegiado = numCol;
        End $$
Delimiter ;

-- Editar Doctores -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EditarDoctor(in numCol int, in nomDoc varchar(50), in apellDoc varchar(50), in telCon varchar(8))
		Begin
			Update Doctor D set
                D.nombresDoctor = nomDoc,
                D.apellidosDoctor = apellDoc,
                D.telefonoContacto = telCon
			where numeroColegiado = numCol;
        End $$
Delimiter ;

-- Recetas
-- Agregar Recetas -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_AgregarReceta(in fechaReceta date, in numeroColegiado int)
		Begin
			Insert Into Receta(fechaReceta, numeroColegiado)
			values (fechaReceta, numeroColegiado);
        End $$
Delimiter ;

Call sp_AgregarReceta("2022-01-10", 12);
Call sp_AgregarReceta("2011-02-11", 36);
Call sp_AgregarReceta("2004-04-21", 50);
Call sp_AgregarReceta("2005-09-14", 48);
Call sp_AgregarReceta("2018-02-19", 84);
Call sp_AgregarReceta("2002-04-10", 74);
Call sp_AgregarReceta("2020-05-17", 95);
Call sp_AgregarReceta("2021-06-18", 64);
Call sp_AgregarReceta("2012-12-05", 95);
Call sp_AgregarReceta("2017-11-08", 64);
Call sp_AgregarReceta("2014-07-06", 101);


-- Listar Recetas -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_ListarRecetas()
		Begin
			Select
				R.codigoReceta,
                R.fechaReceta,
                R.numeroColegiado
			From Receta R;
        End $$
Delimiter ;

Call sp_ListarRecetas();

-- Buscar Receta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_BuscarReceta(in codRec int)
		Begin
			Select
				R.codigoReceta,
                R.fechaReceta,
                R.numeroColegiado
			From Receta R 
				where codigoReceta = codRec;
        End $$
Delimiter ;

Call sp_BuscarReceta(1);

-- Eliminar Receta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EliminarReceta(in codRec int)
		Begin
			Delete From Receta
				where codigoReceta = codRec;
        End $$
Delimiter ;

-- Editar Receta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EditarReceta (in codRec int, in fechRec date)
		Begin
			Update Receta R set
				R.fechaReceta = fechRec
			where codigoReceta = codRec;
        End $$
Delimiter ;

-- Citas
-- Agregar CIta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_AgregarCita(in fechaCita date, in horaCita time, in tratamiento varchar(50), in descripCondActual varchar(100), in codigoPaciente int, in numeroColegiado int)
		Begin	
			Insert Into Cita(fechaCita, horaCita, tratamiento, descripCondActual, codigoPaciente, numeroColegiado)
			values (fechaCita, horaCita, tratamiento, descripCondActual, codigoPaciente, numeroColegiado);
        End $$
Delimiter ;

Call sp_AgregarCita("2021-05-13", "13:50:00", "Tomar Medicamento", "Estable", 101, 12);
Call sp_AgregarCita("2022-01-08", "05:58:00", "Hacer Examenes", "Estable", 102, 36);
Call sp_AgregarCita("2011-03-18", "15:59:00", "Hacer Reposo", "Estable", 103, 95);
Call sp_AgregarCita("2021-05-28", "18:17:00", "Tomar Medicamento", "Delicado", 104, 84);
Call sp_AgregarCita("2008-06-18", "01:28:00", "Hacer Examenes", "Estable", 105, 50);
Call sp_AgregarCita("2015-07-17", "08:26:00", "Tomar Medicamentos", "Estable", 106, 101);
Call sp_AgregarCita("2017-08-24", "14:15:00", "Hacer Examenes", "Estable", 107, 84);
Call sp_AgregarCita("2004-07-09", "13:48:00", "Hacer Reposo", "Delicado", 108, 48);
Call sp_AgregarCita("2006-08-17", "12:19:00", "Hacer Examenes", "Delicado", 109, 95);
Call sp_AgregarCita("2018-09-15", "23:18:00", "Hacer Reposo", "Estable", 110, 74);

-- Listar CIta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_ListarCitas()
		Begin
			Select
				C.codigoCita,
				C.fechaCita,
                C.horaCita,
                C.tratamiento,
                C.descripCondActual,
                C.codigoPaciente,
                C.numeroColegiado
			From Cita C;
        End $$
Delimiter ;

Call sp_ListarCitas();

-- Buscar CIta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_BuscarCita(in codCi int)
		Begin
			Select
				C.codigoCita,
				C.fechaCita,
                C.horaCita,
                C.tratamiento,
                C.descripCondActual,
                C.codigoPaciente,
                C.numeroColegiado
			From Cita C
				where codigoCita = codCi;
        End $$
Delimiter ;

Call sp_BuscarCita(1);

-- Eliminar CIta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EliminarCita(in codCi int)
		Begin
			Delete From Cita
				where codigoCita = codCi;
        End $$
Delimiter ;

-- Editar CIta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EditarCita(in codCi int, in fechCita date, in horCita time, in tratami varchar(50), in condActual varchar(100))
		Begin
			Update Cita C set
				C.fechaCita = fechCita,
				C.horaCita = horaCita,
				C.tratamiento = tratami,
				C.descripCondActual = condActual
			where codigoCita = codCi;
        End $$
Delimiter ;

-- Detalle Receta
-- Agregar Detalle Receta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_AgregarDetalleReceta(in dosis varchar(100), in codigoReceta int, in codigoMedicamento int)
		Begin
			Insert Into DetalleReceta (dosis, codigoReceta, codigoMedicamento)
            values (dosis, codigoReceta, codigoMedicamento);
        End $$
Delimiter ;

Call sp_AgregarDetalleReceta("Tomar cada 8 horas", 1, 2);
Call sp_AgregarDetalleReceta("Tomar cada 2 dias", 2, 4);
Call sp_AgregarDetalleReceta("Tomar cada 10 dias", 8, 5);
Call sp_AgregarDetalleReceta("Tomar cada 6 horas", 3, 2);
Call sp_AgregarDetalleReceta("Tomar en ayunos",5, 1);
Call sp_AgregarDetalleReceta("Tomar despues de cada comida", 2, 3);


-- Listar Detalle Receta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_ListarDetallesRecetas()
		Begin
			Select
				DR.codigoDetalleReceta,
				DR.dosis,
                DR.codigoReceta,
                DR.codigoMedicamento
			From DetalleReceta DR;
        End $$
Delimiter ;

Call sp_ListarDetallesRecetas();

-- Buscar Detalle Receta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_BuscarDetalleReceta(in codDR int)
		Begin
			Select
				DR.codigoDetalleReceta,
				DR.dosis,
                DR.codigoReceta,
                DR.codigoMedicamento
			From DetalleReceta DR
				where codigoDetalleReceta = codDR;
		End $$
Delimiter ;

-- Eliminar Detalle Receta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EliminarDetalleReceta(in codDR int) 
		Begin
			Delete From DetalleReceta
				where codigoDetalleReceta = codDR;
        End$$
Delimiter ;

-- Editar Detalle Receta -----------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_EditarDetalleReceta(in codDR int, in dos varchar(100))
		Begin
			Update DetalleReceta DR set
				DR.dosis = dos
			where codigoDetalleReceta = codDR;
        End$$
Delimiter ;

-- Usuario
-- Agregar Usuario ----------------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create 	Procedure sp_AgregarUsuario( in nombreUsario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(50), in contrasena varchar(50))
		Begin
			Insert Into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
			Values(nombreUsario, apellidoUsuario, usuarioLogin, contrasena);
        End$$
Delimiter ;

Call sp_AgregarUsuario('Selvin', 'Chuquiej', 'schuquiej', '2018354');

-- Listar Usuarios ----------------------------------------------------------------------------------------------------------------------

Delimiter $$
	Create Procedure sp_ListarUsuarios()
		Begin
			Select
				U.codigoUsuario,
                U.nombreUsuario, 
                U.apellidoUsuario, U.usuarioLogin, 
                U.contrasena
			From Usuario U;
        End$$
Delimiter ;

Call sp_ListarUsuarios();

Select * From Receta;
Select * From DetalleReceta;
Select * From Medicamento;
Select * From Doctor;
Select * From Paciente;
Select * From Cita;