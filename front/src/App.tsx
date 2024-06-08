import React from "react";
import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import DefaultLayout from "./layouts/DefaultLayout";
import ProtectedLayout from "./layouts/ProtectedLayout";
import DoctorLayout from "./layouts/DoctorLayout";
import PatientLayout from "./layouts/PatientLayout";
import ExaminationTable from "./components/ExaminationTable";
import DiagnosisTable from "./components/DiagnosisTable";
import TherapyTable from "./components/TherapyTable";
import AlarmTable from "./components/AlarmTable";
import PatientTable from "./components/PatientTable";
import ScheduleExamination from "./components/ScheduleExamination";
import UpdateExamination from "./components/UpdateExamination";
import AdminLayout from "./layouts/AdminLayout";
import AddDoctor from "./components/AddDoctor";
import DoctorTable from "./components/DoctorTable";

function App() {
  return (
    <>
      <Routes>
        <Route element={<DefaultLayout />}>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
        </Route>
        <Route element={<ProtectedLayout />}>
          <Route path="/" element={<Home />} />
        </Route>
        <Route element={<DoctorLayout />}>
          <Route path="/doctor" element={<Home />} />
          <Route path="/doctor/scheduledExaminations" element={<ExaminationTable fetchType={1}/>} />
          <Route path="/doctor/doneExaminations" element={<ExaminationTable fetchType={2}/>} />
          <Route path="/doctor/patients" element={<PatientTable />} />
          <Route path="/doctor/addExamination" element={<ScheduleExamination />} />
          <Route path="/doctor/updateExamination" element={<UpdateExamination />} />
        </Route>
        <Route element={<PatientLayout />}>
          <Route path="/patient" element={<Home />} />
          <Route path="/patient/scheduledExaminations" element={<ExaminationTable fetchType={3}/>} />
          <Route path="/patient/doneExaminations" element={<ExaminationTable fetchType={4}/>} />
          <Route path="/patient/diagnosis" element={<DiagnosisTable />} />
          <Route path="/patient/alarms" element={<AlarmTable />} />
          <Route path="/patient/therapies" element={<TherapyTable />} />
        </Route>
        <Route element={<AdminLayout />}>
          <Route path="/admin" element={<Home />} />
          <Route path="/admin/addDoctor" element={<AddDoctor />} />
          <Route path="/admin/doctors" element={<DoctorTable />} />
          <Route path="/admin/highRiskPatients" element={<PatientTable />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;
