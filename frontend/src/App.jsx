import { Routes, Route } from 'react-router-dom'
import Navbar from './components/Navbar'
import ProtectedRoute from './components/ProtectedRoute'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import CommunityPage from './pages/CommunityPage'
import PostDetail from './pages/PostDetail'
import CreatePost from './pages/CreatePost'
import CreateCommunity from './pages/CreateCommunity'
import Profile from './pages/Profile'
import SearchResults from './pages/SearchResults'

export default function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/r/:name" element={<CommunityPage />} />
        <Route path="/post/:id" element={<PostDetail />} />
        <Route path="/u/:username" element={<Profile />} />
        <Route path="/search" element={<SearchResults />} />
        <Route path="/submit" element={<ProtectedRoute><CreatePost /></ProtectedRoute>} />
        <Route path="/communities/new" element={<ProtectedRoute><CreateCommunity /></ProtectedRoute>} />
      </Routes>
    </>
  )
}
